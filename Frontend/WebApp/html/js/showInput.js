function showSearch(){
    $("#searchForm").toggleClass("visible").focus();
}

$(document).ready(function(){
    $("#search").click(function(){
        showSearch();
    })
})
