(ns sample.models.book
   (:require [clojure.java.jdbc :as sql]
             [sample.db :refer :all]))

 (defn get-book-by-id [id]
   (sql/query db
              ["SELECT * FROM book WHERE id = ?", id]
              {:result-set-fn first}
              ))

 (defn get-book-by-user [user-id]
   (sql/query db
              ["SELECT * FROM book WHERE user_id = ?", user-id]
              ;;{:result-set-fn last}
              ))
 
 (defn get-all-books []
   (sql/query db
    ["SELECT * FROM book"]
   ;;{:result-set-fn identity
   ))
 
(defn parse-int [s]
  (Integer. (re-find  #"\d+" s)))
 
(defn update-book [user-id book-id new-data]
  (let [data (-> new-data (assoc :user_id user-id) )]
    (sql/update! db :book (dissoc data :id) 
                 ["id = CAST(? AS INTEGER) AND user_id = ?"
                  (Integer. (:id data))
                  user-id])))

 
(defn create-book [user-id book]
  (let [data (-> book (assoc :user_id user-id) )]
    (println data)
     (sql/insert! db :book data)))

(defn delete-book [id]
  (sql/delete! db :book ["id = ?", id]))

(defn delete-book-by-user [user-id]
  (sql/delete! db :book ["user_id = ?", user-id]))

