function landingClick(){
    $("#logo").removeClass("land");
    $(".navbar").removeClass("land");
    $("#search").removeClass("land");
    $(".dl").fadeOut();
}

$(window).click(function(){
    landingClick();
})
