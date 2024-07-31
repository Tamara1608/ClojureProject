
(ns sample.views.home
  (:require [hiccup.element :refer :all]
            [sample.views.layout :refer [common]]
            [clojure.tools.logging :as log]))

(defn home [user req]
  (let [user-id (get-in user [:id])]
    (log/info "user id " user-id) 
    (common
     [:div.container
      [:h1.text-center "RemoveBG"]
      [:p.text-center "Welcome, " (or (:name user) "Guest") "!"]
      (if (:id user)
        [:div
         [:form {:id "upload-form" :action "/home/upload" :method "post" :enctype "multipart/form-data" :class "mb-4"}
          [:div.form-group
           [:label {:for "file"} "Upload Image"]
           [:input {:type "file" :name "file" :id "file" :class "form-control"}]]
          [:button {:type "submit" :class "btn btn-primary"} "Upload"]]
         [:div.text-center
          [:canvas {:id "image-canvas" :width "800" :height "600" :class "border"}]
          [:script {:type "text/javascript"}
           (str "document.getElementById('upload-form').onsubmit = function(event) {"
                "  event.preventDefault();"
                "  var formData = new FormData(this);"
                "  fetch('/home/upload', {"
                "    method: 'POST',"
                "    body: formData"
                "  }).then(function(response) {" 
                "console.log ('Status:', response.status);"
                "console.log ('Headers:', response.headers);"
                "    if (response.ok) {"
                "      return response.blob();"
                "    } else {"
                "      throw new Error('Background removal failed');"
                "    }"
                "  }).then(function(blob) {"
                "    var canvas = document.getElementById('image-canvas');"
                "    var context = canvas.getContext('2d');"
                "    var img = new Image();"
                "    img.onload = function() {"
                "      context.drawImage(img, 0, 0, canvas.width, canvas.height);"
                "    };"
                "    img.src = URL.createObjectURL(blob);"
                "  }).catch(function(error) {"
                "    console.error('Error:', error);"
                "  });"
                "};")]
                ]]
        [:div.alert.alert-info {:role "alert"}
         [:strong "Register now! "] "Get your background removed!"])]
     user)))

