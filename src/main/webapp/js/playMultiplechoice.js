const url = new URL(window.location.href);
const schoolClass =  url.searchParams.get("class");
const imageOfStudent = document.getElementById("playMultiImage");
var students = [];
var allStudents = [];
var generatedNames = [];
var generatedButtons = [];
var chosenStudent;
var rightanswers = 0;
var wronganswers = 0;
const namesContainer = document.getElementById("randomNames");
var nextB = document.getElementById("nextChoice");
document.getElementById("pageTitle").innerHTML = "Multiple choice " + schoolClass;
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
                allStudents.push(data[i].studentFirstName + " " + data[i].studentLastName);
            }
        }
        nextStudent();
    });
function getFiveNames(answerName) {
    var arr = [];
    while(arr.length < 4) {
        var name = allStudents[Math.floor(Math.random() * allStudents.length)];
        if(!arr.includes(name) && name !== answerName)  {
          arr.push(name);
      }
    }
    arr.splice(Math.floor(Math.random() * 4), 0, answerName)
    return arr;
}
function getRandomStudent() {
    nextB.disabled = true;
    return students[Math.floor(Math.random() * students.length)];
}
function nextStudent() {
    if(students.length > 0){
        while (namesContainer.firstChild) {
            namesContainer.removeChild(namesContainer.firstChild);
        }
        generatedButtons = [];
        generatedNames = [];
        const student = getRandomStudent();
        createNames(getFiveNames(student.studentFirstName + " " + student.studentLastName))
        students.splice(students.indexOf(student), 1);
        chosenStudent = student;
        imageOfStudent.src = student.portraitPath;
    } else {
        sendScore(getScore(wronganswers, rightanswers));
    }
}
function sendScore(score){
    window.location.href = './evaluation.html?score=' + score + "&class=" + schoolClass;

}
function checkStudent(guess, buttons) {
    for (let i = 0; i < buttons.length; i++) {
            buttons[i].disabled = true;
    }
    var rightName = chosenStudent.studentFirstName + " " + chosenStudent.studentLastName;
    if(guess.value === rightName) {
        guess.style.backgroundColor = "green";
        guess.style.color = "white";
        rightanswers++;
    } else {
        guess.style.backgroundColor = "red";
        console.log(rightName);
        console.log(generatedNames);
        console.log(generatedButtons);
        generatedButtons[generatedNames.indexOf(rightName)].style.backgroundColor = "green";
        generatedButtons[generatedNames.indexOf(rightName)].style.color = "white";
        wronganswers++;
    }
    console.log(rightanswers + " / " + wronganswers);
}
function getScore(wrong, right){
    var total = wrong + right;
    var percentage = 100*(right / total);
    return Math.round(percentage);
}
function createNames(names) {
    for(var i = 0; i < names.length; i++) {
        const button = document.createElement("button");
        button.setAttribute("type", "submit");
        button.setAttribute("value", names[i]);
        button.setAttribute("class", "nameButton");
        button.textContent = names[i];
        generatedNames.push(button.value);
        generatedButtons.push(button);
        namesContainer.appendChild(button);
        button.addEventListener(
            "click", () => {
                checkStudent(button, generatedButtons)
                nextB.disabled = false;
            }
        )
    }
}
