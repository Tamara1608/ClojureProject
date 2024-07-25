(ns sample.views.home
  (:require [hiccup.element :refer :all]
            [sample.views.layout :refer [common]]))
  

(defn home [user]
  (common
   [:div
    [:h1 "RemoveBG"]
    [:p "Welcome, " (or (:name user) "Guest") "!"]
    (if (:id user)
      [:div
       [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
        [:div.form-group
         [:label {:for "file"} "Upload Image"]
         [:input {:type "file" :name "file" :id "file" :class "form-control"}]]
        [:button {:type "submit" :class "btn btn-primary"} "Upload"]]]
      [:div {:class "alert alert-info" :role "alert"}
       [:strong "Register now! "] "Get your background removed!"])]
   user))