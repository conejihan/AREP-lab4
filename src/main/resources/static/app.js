var imageButton = document.getElementById("button");
var nameButton = document.getElementById("buttonName");
var url = "/app/hello?value=";

imageButton.addEventListener('click', function () {
    $('#image').toggle('slow');
});

nameButton.addEventListener('click', function () {
    $("#hello").empty();
    var name = document.getElementById("name").value;
    if(name===""){
        alert("Escribe un nombre en el campo");
    } else {
        axios.get(url+name)
            .then(res => {
                $("#hello").append(res.data);
            }).catch(err => {
            console.log(err);
        });
    }
});
