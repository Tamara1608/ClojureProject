(ns sample.routes.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :as response]
            [sample.models.user :as user]
            [sample.views.home :refer [home]]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]))

(defn call-background-removal-api [image-url] 
  (log/info "Image url:" image-url) 

  (let [url "https://app-studio-editor-api-staging-002.azurewebsites.net/api/background-removal"
        headers {"Accept" "*/*"
                 "Content-Type" "application/json"
                 "x-hunch-identity" "b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"}
        body (json/write-str {:imageUrlsWithCategories [{:imageUrl image-url}]}) 
        response (client/post url
                              {:headers headers
                               :body body 
                               })]
    
    (println "Status:" (:status response))
    (println "Body:" (:body response))
    (log/info "API Request URL:" url)
    (log/info "API Request Headers:" headers)
    (log/info "API Request Body:" body)
    (log/info "API Response Status:" (:status response))
    (log/info "API Response Body:" (:body response))
    response))


(defn upload-handler [req] 
  (log/info "req:" req)
  (let [file (get-in req [:multipart-params "file"]) 
        temp-file (:tempfile file)
        file-name (:filename file)] 
    (log/info "Temp file:" temp-file)
    (if temp-file
      (let [output-path (str "resources/public/uploads/" file-name)
            image-url (str "http://localhost:3000/uploads/" file-name)]
        (io/copy temp-file (io/file output-path)) ; Save the file
        ;; (log/info "File saved to:" output-path) 
         (log/info "img url:" image-url)
        (let [api-response (call-background-removal-api image-url)]
          (if (= 200 (:status api-response))
            (response/response (str "<img src=\"" image-url "\" alt=\"Processed Image\"/>")) ; Display the image
            (response/response "<p>Error processing image. Please try again.</p>"))))
      (response/response "<p>No file uploaded. Please try again.</p>"))))

(defroutes home-routes
  (GET "/" {{:keys [user-id]} :session}
    (home (user/get-user-by-id user-id)))
  (POST "/upload" req (upload-handler req)))
