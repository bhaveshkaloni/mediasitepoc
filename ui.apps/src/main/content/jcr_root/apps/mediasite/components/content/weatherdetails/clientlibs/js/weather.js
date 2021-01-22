$(document).ready(function() {

$('#button').click(function(){
    $.ajax({ 
        url: '/bin/getWeatherDetails',
        type: 'get',
        success: function(result)
        {
            console.log(result.temp);
             $("#content").html("Temperature is::"+result.temp+" degree celcious");
        }
    });
});

});


