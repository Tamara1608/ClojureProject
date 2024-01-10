(ns sample.views.book
  (:require [hiccup.element :refer :all]
            [hiccup.form :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sample.helpers :refer [input-control]]))

(defn add-book-page [& [errors]]
  [:div
   [:h1 "Add a Book"]
   (form-to {:enctype "multipart/form-data"} [:post "/addBook"]
            (anti-forgery-field)
            [:div {:class "form-group"}
             (input-control text-field "title" "Title" nil true (:title errors))]
            [:div {:class "form-group"}
             (input-control text-field "author" "Author" nil true (:author errors))]
            [:div {:class "form-group"}
             (input-control text-field "pages" "Pages" nil true (:pages errors))]
            [:div {:class "form-group"}
             (input-control text-field "price" "Price" nil true (:price errors))]
            [:div {:class "form-group"}
             (input-control text-field "picture_url" "Picture" nil true (:price errors))]
            [:div
             (submit-button {:class "btn btn-primary"} "Add Book")]
            [:div
             [:a {:class "btn btn-secondary" :href "/"} "Exit"]]
            )])

(defn book-edit-page [book] 
  [:div
   [:h1 "Edit book"]
   (form-to {:enctype "multipart/form-data"} [:post "/updateBook"]
            (anti-forgery-field)
            [:div {:class "form-group"}
             (input-control text-field "title" "Title" (:title book) true nil)]
            [:div {:class "form-group"}
             (input-control text-field "author" "Author" (:author book) true nil)]
            [:div {:class "form-group"}
             (input-control text-field "pages" "Pages" (:pages book) true nil)]
            [:div {:class "form-group"}
             (input-control text-field "price" "Price" (:price book) true nil)]
            [:div {:class "form-group"}
             (input-control text-field "picture_url" "Picture" (:picture_url book) true nil)]
            [:div {:class "no-show" :style "display: none"}
             (input-control text-field "id" "ID" (:id book) true nil)] 
            [:div
             (submit-button {:class "btn btn-primary"} "Update Book")]
            )])


(defn script []
  [:script
   "document.addEventListener('DOMContentLoaded', function() {"
   "  var toggleButton = document.getElementById('toggle-add-book-form');"
   "  var form = document.getElementById('add-book-form');"
   "  toggleButton.addEventListener('click', function() {"
   "    form.style.display = form.style.display === 'none' ? 'block' : 'none';"
   "  });"
   "});"])
