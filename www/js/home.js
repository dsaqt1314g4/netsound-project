

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