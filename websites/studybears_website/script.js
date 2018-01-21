/*
uses JQuery
*/

function onSignIn(googleUser)
{
	var profile=googleUser.getBasicProfile();
	$(".g-signin2").css("display","none");
	$(".data").css("display","block");
	$("#pic").attr("src",profile.getImageUrl());
	$("#email").text(profile.getEmail());
	window.location.href = "main.css";
}

function signOut()
{
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function(){

		alert("You have been successfully signed out");

		$(".g-signin2").css("display", "block");
		$(".data").css("display", "none")
	});
}

/* dropdown */
function toggleDropDown() {
    document.getElementById("myDropdown").classList.toggle("show");
}



// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}



