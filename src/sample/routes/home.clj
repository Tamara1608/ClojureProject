(ns sample.routes.home
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [clojure.string :refer [last-index-of]]
            [clojure.tools.logging :as log]
            [compojure.core :refer [defroutes GET POST]]
            [org.httpkit.client :as http]
            [ring.util.response :as response]
            [sample.models.user :as user]
            [sample.views.home :refer [home]]))


(defn call-background-removal-api [file-path callback]
  (let [url "http://127.0.0.1:8000/predict"
        file (java.io.File. file-path)
        original-file-name (.getName file)
        base-name (subs original-file-name 0 (last-index-of original-file-name "."))
        output-file (str "resources/public/uploads/" base-name ".png")  ;
        multipart-data [{:name "image_file"
                         :content file
                         :filename (.getName file)
                         :mime-type "image/jpeg"}]] 
    (http/post url
               {:multipart multipart-data}
               (fn [{:keys [status body] :as resp}] 
                 (when (= 200 status)
                   (with-open [out (io/output-stream output-file)]
                     (io/copy body out))
                   (log/info output-file)
                   (callback {:status status :body output-file}))
                 (when (not= 200 status)
                   (callback {:status status :body (slurp body)}))))))

(defn upload-handler [req]
  (let [file (get-in req [:multipart-params "file"])
        temp-file (:tempfile file)
        file-name (:filename file)]
    (if temp-file
      (let [output-path (str "resources/public/uploads/" file-name)]
        (io/copy temp-file (java.io.File. output-path))
        (call-background-removal-api output-path 
                                     (fn [{:keys [status body]}] 
                                       (if (= 200 status)
                                         (-> (response/response body)
                                             (assoc :headers {"Content-Type" "image/png"}))
                                         (response/status (response/response "Background removal failed") 500)))))
      (response/status (response/response "File upload failed") 400))))


(defroutes home-routes 
    (GET "/home" req
      (let [session (:session req)]
        (let [{:keys [user-id]} session]
          (home (user/get-user-by-id user-id) req)))) 

     (POST "/home/upload" req (upload-handler req)))