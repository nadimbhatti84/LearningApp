const viewElements = document.getElementsByClassName("viewClass");
const learnElements = document.getElementsByClassName("learnClass")
function sendView(content) {
    window.location.href = "./schoolClass.html?class=" + content;
}
function sendLearn(content) {
    window.location.href = "./play.html?class=" + content;
}
for(var i = 0; i < viewElements.length; i++) {
    const value = viewElements[i].value
    viewElements[i].addEventListener('click', ()=> sendView(value));
    learnElements[i].addEventListener('click', ()=> sendLearn(value));
}