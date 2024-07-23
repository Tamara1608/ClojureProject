(ns sample.routes.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :as response]
            [sample.models.user :as user]
            [sample.views.home :refer [home]]))


(defn process-image [file]
  ;; Placeholder for actual image processing logic
  (let [image-path (str "uploads/" (:filename file))]
    (spit image-path (:tempfile file)) ; Save the file for now
    image-path))

(defn upload-handler [req]
  (let [file (get-in req [:params "image"])
        processed-image-path (process-image file)]
    (response/response (str "<img src=\"" processed-image-path "\" alt=\"Processed Image\"/>"))))

(defroutes home-routes
  (GET "/" {{:keys [user-id]} :session}
    (home (user/get-user-by-id user-id)))  
  (POST "/upload" req (upload-handler req)))
