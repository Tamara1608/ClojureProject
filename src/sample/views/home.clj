(ns sample.views.home
  (:require [hiccup.element :refer :all]
            [sample.views.layout :refer [common]]
            [clojure.tools.logging :as log]))

(defn home [user req]
  (print "req" req)
  (let [processed-image-url (get-in (:session req) [:processed-image-url])]
    (log/info "Processed image URL:" processed-image-url)
    (common
     [:div.container
      [:h1.text-center "RemoveBG"]
      [:p.text-center "Welcome, " (or (:name user) "Guest") "!"]
      (if (:id user)
        [:div
         [:form {:action "/upload" :method "post" :enctype "multipart/form-data" :class "mb-4"}
          [:div.form-group
           [:label {:for "file"} "Upload Image"]
           [:input {:type "file" :name "file" :id "file" :class "form-control"}]]
          [:button {:type "submit" :class "btn btn-primary"} "Upload"]] 
         [:div.text-center
             [:canvas {:id "image-canvas" :width "800" :height "600" :class "border"}]
             [:script {:type "text/javascript"}
              (str "window.onload = function() {"
                   "  var canvas = document.getElementById('image-canvas');"
                   "  var context = canvas.getContext('2d');"
                   "  var img = new Image();"
                   "  img.onload = function() {"
                   "    context.drawImage(img, 0, 0, canvas.width, canvas.height);"
                   "  };"
                   "  img.src = '" (or processed-image-url "") "';"
                   "};")]]]
        [:div.alert.alert-info {:role "alert"}
         [:strong "Register now! "] "Get your background removed!"])]
     user)))
