import {customPath} from './config.js';
const url = new URL(window.location.href);
const schoolClass =  url.searchParams.get("class")
document.getElementById("pageTitle").innerHTML = schoolClass;

document.getElementById("cancel").addEventListener("click", () => {
    window.location.href = './index.html';
})

console.log(customPath);

var container = document.getElementById("classContainer");
fetch(
    './resource/student/list'
)  .then((response) => response.json())
    .then((data) => {
        var students = []
        for (var i = 0; i < data.length; i++) {
            var splitted = data[i].portraitPath.split('/');
            if(splitted[2] === schoolClass) {
                students.push(data[i].studentID);
            }
        }
        console.log(data);
        for (var i = 0; i < data.length; i++) {
            for (var j = 0; j < students.length; j++) {
                if(data[i].studentID === students[j]) {
                    var divEl = document.createElement('div');
                    divEl.setAttribute('class', 'studentElement');

                    var imgEl = document.createElement('img');
                    imgEl.setAttribute('class', 'picture');
                    imgEl.src = customPath +  data[i].portraitPath;

                    var pEl = document.createElement('p');
                    pEl.setAttribute('class', 'studentName');
                    pEl.textContent = data[i].studentFirstName + " " + data[i].studentLastName;

                    divEl.appendChild(imgEl);
                    divEl.appendChild(pEl);
                    container.appendChild(divEl);
                }
            }
        }
    });


