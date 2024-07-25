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
            ;; [cheshire.core :as json]
            [clojure.data.json :as json]))

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

(def app
  (-> (routes
       auth-routes
       home-routes
       profile-routes
       files-routes
       library-routes
       static-routes)
       wrap-multipart-params
       (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
