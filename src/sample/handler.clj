;; (ns sample.handler
;;   (:require [compojure.core :refer :all]
;;             [compojure.route :as route]
;;             [migratus.core :as migratus]
;;             [sample.routes.home :refer [home-routes]]
;;             [sample.routes.profile :refer [profile-routes]]
;;             [sample.routes.auth :refer [auth-routes]]
;;             [sample.routes.library :refer [library-routes]]
;;             [sample.routes.files :refer [files-routes]]
;;             [sample.views.layout :as layout]
;;             [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
;;             [ring.middleware.multipart-params :refer [wrap-multipart-params]]
;;             [ring.util.response :refer [response]]
;;             [clojure.java.shell :as shell]
;;             [clojure.java.io :as io]
;;             [clj-http.client :as client]
;;             [cheshire.core :as json]))

;; (def migratus-config
;;   {:store :database
;;    :migration-dir "migrations"
;;    :db "postgresql://postgres:1234@localhost:5432/database"})

;; (defn init []
;;   (migratus/migrate migratus-config))

;; (defn not-found []
;;   (layout/base
;;    [:center
;;     [:h1 "404. Page not found!"]]))

;; (defroutes static-routes
;;   (route/resources "/")
;;   (route/not-found (not-found)))

;; ;; Function to call the background removal API
;; (defn call-background-removal-api [image-url]
;;   (let [url "https://staging-bg-removal-1.hunchads.com/api/background-removal"
;;         payload {:teamId "2"
;;                  :imageUrlsWithCategories [{:imageUrl image-url}]}
;;         response (client/post url
;;                               {:body (json/generate-string payload)
;;                                :headers {"Content-Type" "application/json"
;;                                           "Authorization" "b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"}})]
;;     (json/parse-string (:body response) true)))

;; ;; Handler function to manage image uploads
;; (defn upload-image-handler [request]
;;   (let [file (get-in request [:params :file])
;;         temp-file (:tempfile file)
;;         file-name (:filename file)
;;         output-path (str "resources/public/uploads/" file-name)]
;;     (try
;;       ;; Save the uploaded file
;;       (io/copy temp-file (io/file output-path))
;;       ;; Generate the URL for the uploaded image
;;       (let [image-url (str "http://localhost:3000/" file-name) 
;;             api-response (call-background-removal-api image-url)]
;;         (if (get api-response "status" nil)
;;           (response {:status "success"
;;                      :result (get api-response "result" nil)})
;;           (response {:status "error"
;;                      :message "Failed to process image"})))
;;       (catch Exception e
;;         (response {:status "error" :message (.getMessage e)})))))

;; (defroutes image-routes
;;   (POST "/upload" request (upload-image-handler request)))

;; (def app-routes
;;   (routes
;;    auth-routes
;;    home-routes
;;    profile-routes
;;    files-routes
;;    library-routes
;;    image-routes
;;    static-routes))

;; (def app
;;   (-> app-routes
;;       wrap-multipart-params
;;       (wrap-defaults site-defaults)))


(ns sample.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [migratus.core :as migratus]
            [sample.routes.home :refer [home-routes]]
            [sample.routes.profile :refer [profile-routes]]
            [sample.routes.auth :refer [auth-routes]]
            [sample.routes.library :refer [library-routes]]
            [sample.routes.files :refer [files-routes]]
            [sample.views.layout :as layout]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [clj-http.client :as client]
            [cheshire.core :as json]))

(def migratus-config
  {:store :database
   :migration-dir "migrations"
   :db "postgresql://postgres:1234@localhost:5432/database"})

(defn init []
  (migratus/migrate migratus-config))

(defn not-found []
  (layout/base
   [:center
    [:h1 "404. Page not found!"]]))

(defroutes static-routes
  (route/resources "/")
  (route/not-found (not-found)))

;; Function to call the background removal API
(defn call-background-removal-api [image-url]
  (let [url "https://staging-bg-removal-1.hunchads.com/api/background-removal"
        payload {:teamId "2"
                 :imageUrlsWithCategories [{:imageUrl image-url}]}
        response (client/post url
                              {:body (json/generate-string payload)
                               :headers {"Content-Type" "application/json"
                                         "Authorization" "Bearer b6ec313ace84511eba266ed396ea166b9525abcd73ac1e2c07c967033ebc895d"}})]
    (json/parse-string (:body response) true)))

;; Handler function to manage image uploads
(defn upload-image-handler [request]
  (let [file (get-in request [:params :file])
        temp-file (:tempfile file)
        file-name (:filename file)
        output-path (str "resources/public/uploads/" file-name)]
    (try
      (io/copy temp-file (io/file output-path))
      ;; Generate the URL for the uploaded image
      (let [image-url (str "http://localhost:3000/uploads/" file-name)
            api-response (call-background-removal-api image-url)]
        (if (get api-response "status")
          (response {:status "success"
                     :result (get api-response "result" nil)})
          (response {:status "error"
                     :message "Failed to process image"})))
      (catch Exception e
        (response {:status "error" :message (.getMessage e)})))))

(defroutes image-routes
  (POST "/upload" request (upload-image-handler request)))

(def app-routes
  (routes
   auth-routes
   home-routes
   profile-routes
   files-routes
   library-routes
   image-routes
   static-routes))

(def app
  (-> app-routes
      wrap-multipart-params
      ;; wrap-defaults is used without anti-forgery middleware
      (wrap-defaults (assoc site-defaults :security nil))))
