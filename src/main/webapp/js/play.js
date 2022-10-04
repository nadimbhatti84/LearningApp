const url = new URL(window.location.href);
const schoolClass =  url.searchParams.get("class");
document.getElementById("pageTitle").innerHTML = "Lernmodus " + schoolClass; 
document.getElementById("cancel").addEventListener("click", () => {
    window.location.href = './index.html';
})

