(ns sample.routes.home
  (:require [clojure.java.io :as io]
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
                         :mime-type "image/jpeg"}] 
  ]
    (http/post url
               {:multipart multipart-data}
               (fn [{:keys [status body] :as resp}]
                 (println "Status:" status)
                 ;; Save the image to a file
                 (when (= 200 status)
                   (with-open [out (io/output-stream output-file)]
                     (io/copy body out))
                   (callback {:status status :body output-file}))
                 (when (not= 200 status)
                   (callback {:status status :body (slurp body)}))))
        ))

(defn upload-handler [req]
  (let [file (get-in req [:multipart-params "file"])
        temp-file (:tempfile file)
        file-name (:filename file)]
    (if temp-file
      (let [output-path (str "resources/public/uploads/" file-name)]
        (io/copy temp-file (java.io.File. output-path))
        (call-background-removal-api output-path
                                     (fn [{:keys [status body]}]
                                       (println "Callback response:" {:status status :body body})
                                       (if (= 200 status)
                                         (let [processed-image-url body]
                                           (-> (response/redirect "/")
                                               (assoc-in [:session :processed-image-url] processed-image-url)))
                                         (response/response "<p>Error processing image. Please try again.</p>")))))
      (response/response "<p>No file uploaded. Please try again.</p>"))))


(defroutes home-routes
 (GET "/" req
   (let [session (:session req)]
     (let [{:keys [user-id]} session]
       (home (user/get-user-by-id user-id) req))))

  (POST "/upload" req (upload-handler req)))
