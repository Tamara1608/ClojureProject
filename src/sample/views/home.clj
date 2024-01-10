(ns sample.views.home
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer :all]
            [sample.models.user :as user]
            [sample.helpers :refer :all]
            [sample.models.book :as book-model]))

(defn home [user all-books]
  [:div
   [:h1 "Libre Nation"]
   [:p "Welcome, " (or (:name user) "Guest") "!"]
   (when (:id user)
     [:a {:href "/add-book" :class "btn btn-primary"} "Add a Book"])
   (let [all-books (book-model/get-all-books)]
       [:div
        [:h1 "Welcome to our Bookstore"] 
        [:div
         [:div {:id "myModal" :class "modal"} ; Modal HTML structure
          [:div {:class "modal-content"}
           [:span.close "&times;"]
           [:p {:id "modalText"}]
           ]]]
       
        (println "books: " all-books)
        (for [book all-books] 
           [:div {:class "book-details"}
            [:img {:src (:picture_url book) :alt "Book Cover" :class "book-cover"}]
            [:div {:class "book-info"}
             [:h4 [:span "Title: "] [:strong (:title book)]]
             [:p [:span "Author: "] [:strong (:author book)]]
             [:p [:span "Pages: "] [:strong (:pages book)]]
             [:p [:span "Price: "] [:strong (:price book)]]
                     ;;  [:a {:href (str "/buyBook/" (:id book)) :class "btn btn-success" :id "openModalBtn"} "Buy"]
             (let [contact (user/get-user-by-id (:user_id book))]
               [:button.btn.btn-success
                {:id "openModalBtn"
                 :onclick (str "openModal('" (:email contact) "');")
                 :style "cursor: pointer;"}
                "Buy"]
               )
             ]])])
           
   ])
               
               
