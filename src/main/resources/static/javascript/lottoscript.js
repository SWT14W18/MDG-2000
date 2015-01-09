$( document ).ready(function() {

	for( var x = 0; x < 12; x++){
	x+=1;
	for( var i = 0; i < 49; i++){
	var a = (i%7);
	var b = Math.trunc(i/7);
	a +=1;
	b +=1;
	i +=1;
    var button = $('<button/>',
    {
        text: i,
        type: 'button',
        click: function() {
            var stringNumber = $(this).attr("id");
        	var number = stringNumber.substring(6,8);
        	var field = stringNumber.substring(9,11);
        	
        	if($(this).attr("active") != 1){
        		$(this).attr("active","1");
        		console.log("active AN!");
        		$(this).toggleClass("angeklickt");
        	}
        	else
        	{
        		$(this).attr("active","0");
        		console.log("active AUS!");
        		$(this).toggleClass("angeklickt");
        	}
        }
    });
    button.addClass("lottoButton");
    if(i < 10 && x < 10) {
    		button.attr("id","lotto-"+'0'+i+"-"+'0'+x); 
    }
    if(i < 10 && x >= 10){
    		button.attr("id","lotto-"+'0'+i+"-"+x);
    }
    if(i >= 10 && x < 10){
      		button.attr("id","lotto-"+i+"-"+'0'+x);
    }
	if(i >= 10 && x >= 10){
			button.attr("id","lotto-"+i+"-"+x);
	}
    button.attr("left",a);
    button.attr("top",b);
 
    console.log(button.attr("id"));
    $('#pat'+x).append(button).end();
    if(b!=7 && a==7){$('#pat'+x).append('<br/>').end();}
    i-=1;
    }
   	$('#pat'+x).attr("width","auto");
    x-=1;
    } 
});

function sub(){
	var tipp = 0;
	var tippschein = 1;
    $(":button").each( function (){
    	if($(this).attr("active")==1){ 
    		tipp+=1;
			var stringNumber = $(this).attr("id");
        	var lottozahl = stringNumber.substring(6,8); //Gewählte Lottozahl
        	var lottoschein = stringNumber.substring(9,11); //Gewählter Tippschein
			if(lottoschein.substring(0,1) == '0'){
				lottoschein = lottoschein.substring(1,2);
			}
			if(Number(lottoschein) > Number(tippschein)) { tippschein = lottoschein; tipp=1;}
			var submitNumber = 'number'+lottoschein+'-'+tipp;
			
			
    		var input = $('<input/>',
    		{
       			name: submitNumber,
        		type: 'hidden',
        		value: lottozahl,
        	});
        	
        	$(this).append(input).end();
		}
    });
}


