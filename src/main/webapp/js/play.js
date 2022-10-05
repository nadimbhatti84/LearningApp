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
    "click", () => {window.location.href = './learnMode.html?class=' + schoolClass;
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
            nextStudent();
        } else {
            location.reload();
        }
    });
function getRandomStudent() {
    nextB.disabled = true;
    return students[Math.floor(Math.random() * students.length)];
}
function nextStudent() {
    if(students.length > 0){
        const student = getRandomStudent();
        console.log(students);
        students.splice(students.indexOf(student), 1);
        chosenStudent = student;
        document.getElementById("correction").disabled = false;
        imageOfStudent.src = student.portraitPath;
        checkB.disabled = false;
        document.getElementById("guess").style.backgroundColor = "white";
        document.getElementById("correction").style.visibility = "hidden";
        document.getElementById("guess").value = "";
    } else {
        sendScore(getScore(wronganswers, rightanswers));
    }
}
function sendScore(score){
    window.location.href = './evaluation.html?score=' + score + "&class=" + schoolClass;

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
    sendScore(getScore(wronganswers, rightanswers));
}
function getScore(wrong, right){
    var total = wrong + right;
    var percentage = right / total;
    return Math.round(percentage);
}
checkB.addEventListener(
    "click", () => checkStudent(chosenStudent)
);
