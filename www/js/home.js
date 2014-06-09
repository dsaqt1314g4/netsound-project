

    $('.search-panel .dropdown-menu').find('a').click(function(e) {
		e.preventDefault();
		var param = $(this).attr("href").replace("#","");
		var concept = $(this).text();
		$('.search-panel span#search_concept').text(concept);
		$('.input-group #search_param').val(param);
	});

    $("#button_search").click(function(e) {
    	e.preventDefault();
    	document.cookie="userid=" + $("#search").val();
    	console.log(document.cookie);
    	
    });
    
    $("#home").click(function(e) {
    	e.preventDefault();
    	window.location.replace("/home.html");
    	
    });
    
    $("#profile").click(function(e) {
    	e.preventDefault();
    	var userprofile = getCookie("userlink");
    	document.cookie="userprofile="+userprofile;
    	window.location.replace("/profile.html");
    	
    });
    

    function delete_cookie( name ) {
    	  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    	}

    function getCookie(name){
    var pattern = RegExp(name + "=.[^;]*");
    matched = document.cookie.match(pattern);
    if(matched){
        var cookie = matched[0].split('=');
        return cookie[1];
    }
    return false;
    }
    
    