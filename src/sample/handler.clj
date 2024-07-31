(ns sample.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [migratus.core :as migratus]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]] ;; [cheshire.core :as json]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.memory :refer [memory-store]]
            [sample.routes.auth :refer [auth-routes]]
            [sample.routes.files :refer [files-routes]]
            [sample.routes.home :refer [home-routes]]
            [sample.routes.profile :refer [profile-routes]]
            [sample.views.layout :as layout]))

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
       static-routes)
      wrap-multipart-params
      (wrap-session {:store (memory-store)})  ;; Add session middleware
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
