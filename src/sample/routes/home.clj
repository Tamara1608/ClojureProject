(ns sample.routes.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :as response]
            [sample.models.user :as user]
            [sample.views.home :refer [home]]
            [org.httpkit.client :as http]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

;; (defn call-background-removal-api [image-url]
;;   (let [url "https://app-studio-editor-api-staging-002.azurewebsites.net/api/background-removal"
;;         headers {"Authorization" "Bearer b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"
;;                  "Content-Type" "application/json"
;;                  "x-hunch-identity" "b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"}
;;         body (json/write-str {:teamId "2"
;;                               :imageUrlsWithCategories [{:imageUrl image-url}]})
;;         options {:headers headers
;;                  :body body
;;                  :content-type :json
;;                  :accept :json}]

;;     (let [response (http/post url options)])
;;      (fn [{:keys [status body] :as resp}]
;;       (log/info "Request payload:" payload)
;;       (log/info "Response:" resp)

;;   ))
(defn call-background-removal-api []
  (let [url "https://staging-bg-removal-1.hunchads.com/api/background-removal"
        payload (json/write-str {:teamId "2"
                                       :imageUrlsWithCategories [{:category "general"
                                                                  :imageUrl "https://images.squarespace-cdn.com/content/v1/60f1a490a90ed8713c41c36c/1629223610791-LCBJG5451DRKX4WOB4SP/37-design-powers-url-structure.jpeg"}]})
        headers {"x-hunch-identity" "b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"
                 "Content-Type" "application/json"
                 "Authorization" "Bearer b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"}] ; Replace YOUR_AUTH_TOKEN with your actual token
    (http/post url {:headers headers
                    :body payload
                    :content-type :json
                    :accept :json}
               
               (fn [{:keys [status body] :as resp}]
                 (log/info "Request payload:" payload)
                 (log/info "Response:" resp)
                 (if (= 200 status)
                   (log/info "Response body:" body)
                   (log/error "Error calling background removal API:" body))))))



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
        (let [api-response (call-background-removal-api )]
          (if (= 200 (:status api-response))
            (response/response (str "<img src=\"" image-url "\" alt=\"Processed Image\"/>")) ; Display the image
            (response/response "<p>Error processing image. Please try again.</p>"))))
      (response/response "<p>No file uploaded. Please try again.</p>"))))

(defroutes home-routes
  (GET "/" {{:keys [user-id]} :session}
    (home (user/get-user-by-id user-id)))
  (POST "/upload" req (upload-handler req)))
