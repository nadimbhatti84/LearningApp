const url = new URL(window.location.href);
const schoolClass =  url.searchParams.get("class");
document.getElementById("cancel").addEventListener(
    "click", () => {
        window.location.href = './index.html';
    })
fetch(
    './resource/schoolClass/list'
)  .then((response) => response.json())
    .then((data) => {
        for (let i = 0; i < data.length; i++) {
            if(data[i].schoolClassName === schoolClass) {
                displayHistory(data[i].learned)
            }
        }
    })
function displayHistory(historyArr) {
    const container = document.getElementById("historyTable");
    for (let i = 0; i < historyArr.length; i++) {
        const tr = document.createElement("tr");
        const tdNr = document.createElement("td");
        tdNr.textContent = i+1;
        const tdValue = document.createElement("td");
        tdValue.textContent = historyArr[i] + "%";
        tr.appendChild(tdNr);
        tr.appendChild(tdValue);
        container.appendChild(tr)
    }
}
