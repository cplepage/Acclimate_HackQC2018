function toggleLeftPane(){
    $("#leftPane").toggleClass("close");
    $("#expander").toggleClass("rotate");
}


$(document).ready(function(){
    $("#expander").click(function(){
        toggleLeftPane();
    })
})
