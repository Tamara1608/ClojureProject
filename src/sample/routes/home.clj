(ns sample.routes.home
  (:require [compojure.core :refer :all]
            [sample.helpers :refer :all]
            [sample.views.home :as view]
            [sample.views.book :as book-view]
            [sample.views.layout :as layout]
            [sample.models.book :as book-model]))


(defn home [user]
  (let [all-books (book-model/get-all-books)]
    (layout/common (view/home user all-books) user)))


(defroutes home-routes
  (GET "/" {{:keys [user-id]} :session}
    (home (get-user user-id))) 
  (GET "/add-book" []
    (layout/common (book-view/add-book-page) nil))
  
  ) 
