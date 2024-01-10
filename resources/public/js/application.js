
$(document).ready(function () {

   // Define the openModal function
  window.openModal = function (contactEmail) {
    console.log("Opening modal for book ID:", contactEmail);

    $("#modalText").text("Please contact seller via mail: " + contactEmail);

    // Show the modal
    $("#myModal").show();
  };

  // When the close button or outside the modal is clicked, hide the modal
  $(".close, .modal").click(function () {
    $("#myModal").hide();
  });

  // Prevent modal from closing when the content inside the modal is clicked
  $(".modal-content").click(function (event) {
    event.stopPropagation();
  });
});


