(ns sample.routes.library
    (:require [compojure.core :refer :all]
              [sample.db :refer :all]
              [sample.models.book :as book-db]
              [ring.util.response :as response]
              [ring.util.codec :refer [url-decode]]
              [sample.views.book :as book-views]
              [clojure.java.jdbc :as sql]
              [sample.views.layout :as layout]
              [sample.views.profile :as view]
              ))


(defn add-book [user-id book-data]
  (let [new-book (book-db/create-book user-id book-data)]))

(defn book-edit-page [ book ] 
  (layout/common (book-views/book-edit-page book)))

(defn update-book [user-id book-id data]
  (let[updated-book (book-db/update-book user-id book-id data)]))

(defn delete-book [id]
  (book-db/delete-book id) )

(defn parse-int [s]
  (Integer. (re-find  #"\d+" s)))


(defroutes library-routes
  
  (POST "/addBook" request
    (let [user-id (:user-id (:session request))
          book-data (:params request)]
      (println "USER ID "user-id)
      (add-book user-id book-data))
    (response/redirect "/add-book"))
  
  (GET "/editBook/:id" [id]
    (book-edit-page (book-db/get-book-by-id (parse-int id)))) 
  
  (POST "/updateBook" request
    (let [user-id (:user-id (:session request))
          book-id (Integer. (:id (:params request)))
          new-data (:params request)] 
      (if (nil? user-id) 
        (println "User id is nil. Session map: " (:session request)) 
        (do 
          (update-book user-id book-id new-data) 
          (response/redirect "/profile"))))) 

  (POST "/deleteBook" request
    (let [user-id (:user-id (:session request))
          book-id-str (:id (:params request))]
      ((let [book-id (Integer. book-id-str)]
          (println "User id: " user-id " Deleting book id: " book-id)
          (delete-book book-id)
          (response/redirect "/profile"))
       )))
  )
