const url = new URL(window.location.href);
const schoolClass =  url.searchParams.get("class");
const imageOfStudent = document.getElementById("playImage");
var students = [];
var chosenStudent;
var rightanswers = 0;
var wronganswers = 0;
var nextB = document.getElementById("next");
var checkB = document.getElementById("check");
document.getElementById("pageTitle").innerHTML = "Lernmodus " + schoolClass;
document.getElementById("cancel").addEventListener(
    "click", () => {window.location.href = './index.html';
})
nextB.addEventListener(
    "click", () => nextStudent()
);

fetch(
    './resource/student/list'
)  .then((response) => response.json())
    .then((data) => {
        for (var i = 0; i < data.length; i++) {
            var splitted = data[i].portraitPath.split('/');
            if(splitted[2] === schoolClass) {
                students.push(data[i]);
            }
        }
        var student = getRandomStudent();
        if(!(student === undefined)){
            startPlay(student);
        } else {
            location.reload();
        }
    });

function getRandomStudent() {
    nextB.disabled = true;
    return students[Math.floor(Math.random() * students.length-1)];
}
function startPlay(student) {
    imageOfStudent.setAttribute('src', student.portraitPath);
    students.splice(students.indexOf(student), 1);
    chosenStudent = student;
}
function nextStudent() {
    if(students.length >= 0){
        const student = getRandomStudent();
        if (!(student === undefined)){
            chosenStudent = student;
            document.getElementById("correction").disabled = false;
            imageOfStudent.src = student.portraitPath;
            students.splice(students.indexOf(student), 1);
            checkB.disabled = false;
            document.getElementById("guess").style.backgroundColor = "white";
            document.getElementById("correction").style.visibility = "hidden";
            document.getElementById("guess").value = "";
            console.log("Richtig: " + rightanswers);
            console.log("Falsch: " + wronganswers);
        } else {
            nextB.click();
        }
    } else {
    }
}
function checkStudent(student) {
    checkB.disabled = true;
    var name = document.getElementById("guess").value;
    if(name === ""){
        alert("Leeres Feld");
        checkB.disabled = false;
    }else {
        nextB.disabled = false;
        var realname = student.studentFirstName + " " + student.studentLastName;
        if (name.toLowerCase() === realname.toLowerCase()) {
            document.getElementById("guess").style.backgroundColor = "green";
            rightanswers++;
        } else {
            document.getElementById("answer").innerText = realname;
            document.getElementById("correction").style.visibility = "visible";
            wronganswers++;
        }
    }
    students.splice(students.indexOf(student), 1);
}
checkB.addEventListener(
    "click", () => checkStudent(chosenStudent)
);
