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
          [:div.form-group.text-center.d-flex.justify-content-center
           [:label {:for "file" :class "btn btn-primary btn-lg mr-2"} "Choose File"
            [:input {:type "file" :name "file" :id "file" :class "form-control" :style "display:none;"
                     :onchange "document.getElementById('file-name').textContent = this.files[0].name;"}]]
           [:span#file-name.align-self-center "No file chosen"]
           [:button {:type "submit" :class "btn btn-success btn-lg ml-2"} "Upload"]]]
         [:div.text-center.mt-4
          [:div#canvas-container {:style "position: relative; width: 100%; max-width: 800px; margin: 0 auto; padding: 10px; border: 4px solid #ccc; box-sizing: border-box;"}
           [:canvas#image-canvas {:width "800" :height "600" :style "display: block; margin: 0 auto; border: 2px solid #ccc;"}]]]
         [:div.text-center.mt-2
          [:button#download-btn.btn.btn-primary {:type "button"} "Download Image"]]
         [:script {:type "text/javascript"}
          (str "document.getElementById('upload-form').onsubmit = function(event) {"
               "  event.preventDefault();"
               "  var formData = new FormData(this);"
               "  fetch('/home/upload', {"
               "    method: 'POST',"
               "    body: formData"
               "  }).then(function(response) {"
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
               "      context.clearRect(0, 0, canvas.width, canvas.height);"
               "      var scale = Math.min(canvas.width / img.width, canvas.height / img.height);"
               "      var x = (canvas.width / 2) - (img.width / 2) * scale;"
               "      var y = (canvas.height / 2) - (img.height / 2) * scale;"
               "      context.drawImage(img, x, y, img.width * scale, img.height * scale);"
               "    };"
               "    img.src = URL.createObjectURL(blob);"
               "    document.getElementById('download-btn').onclick = function() {"
               "      var downloadLink = document.createElement('a');"
               "      downloadLink.style.display = 'none';"
               "      downloadLink.href = canvas.toDataURL('image/png');"
               "      downloadLink.download = 'image.png';"
               "      document.body.appendChild(downloadLink);"
               "      downloadLink.click();"
               "      document.body.removeChild(downloadLink);"
               "    };"
               "  }).catch(function(error) {"
               "    console.error('Error:', error);"
               "  });"
               "};")]]
        [:div.alert.alert-info {:role "alert"}
         [:strong "Register now! "] "Get your background removed!"])]
     user)))
