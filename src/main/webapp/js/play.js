const url = new URL(window.location.href);
const schoolClass =  url.searchParams.get("class");
document.getElementById("pageTitle").innerHTML = "Lernmodus " + schoolClass; 
document.getElementById("cancel").addEventListener("click", () => {
    window.location.href = './index.html';
})

fetch(
    './resource/student/list'
)  .then((response) => response.json())
    .then((data) => {
        console.log(data);

        for (var i = 0; i < data.length; i++) {
            var splitted = data[i].portraitPath.split('/');
            if(splitted[2] === schoolClass) {
                console.log(data[i].portraitPath);
            }
        }


    });
