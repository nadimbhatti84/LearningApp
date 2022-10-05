const url = new URL(window.location.href);
const score =  url.searchParams.get("score");
const schoolClass =  url.searchParams.get("class");
var schoolClassID = '';
document.getElementById("cancel").addEventListener(
    "click", () => {
        window.location.href = './index.html';
    }
);
document.getElementById("retry").addEventListener(
    "click", () => {
        window.location.href = "./play.html?class=" + schoolClass;
    }
)
const scoreEv = document.getElementById("evaluationScore");
scoreEv.innerText += " " + score + "%";

fetch(
    './resource/schoolClass/list'
)  .then((response) => response.json())
    .then((data) => {
        for (var i = 0; i < data.length; i++) {
            if(data[i].schoolClassName === schoolClass) {
                schoolClassID = data[i].schoolClassID;
                console.log(schoolClassID);
            }
        }
        //json can't save 0's --> make it char
        fetch(
            "./resource/schoolClass/addLearned?schoolClassID=" + schoolClassID.toString() +
            "&learnedValue=" + score
        );

    });






