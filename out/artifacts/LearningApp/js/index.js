const container = document.getElementById("ClassContainer");

fetch(
    './resource/schoolClass/list'
)  .then((response) => response.json())
    .then((data) => {
        console.log(data);
        for (var i = 0; i < data.length; i++) {
            const mainDivEl = document.createElement('div');
            mainDivEl.setAttribute('id', data[i].schoolClassName);
            const par = document.createElement('p');
            par.setAttribute('class', 'className');
            par.textContent = data[i].schoolClassName;
            const buttonDiv = document.createElement('div');
            buttonDiv.setAttribute('class', 'buttons');
            const viewB = document.createElement('button');
            viewB.setAttribute('type', 'submit');
            viewB.setAttribute('value', data[i].schoolClassName);
            viewB.setAttribute('class', 'viewClass');
            viewB.textContent = 'Klasse ansehen';
            const learnB = document.createElement('button');
            learnB.setAttribute('type', 'submit');
            learnB.setAttribute('value', data[i].schoolClassName);
            learnB.setAttribute('class', 'learnClass');
            learnB.textContent = 'Diese Klasse lernen';
            const historyB = document.createElement("button");
            historyB.setAttribute('type', 'submit');
            historyB.setAttribute('value', data[i].schoolClassName);
            historyB.setAttribute('class', 'historyClass');
            historyB.textContent = "Verlauf anzeigen";
            buttonDiv.appendChild(historyB);
            buttonDiv.appendChild(viewB);
            buttonDiv.appendChild(learnB);
            mainDivEl.appendChild(par);
            mainDivEl.appendChild(buttonDiv);
            container.appendChild(mainDivEl);
        }

        const viewElements = document.getElementsByClassName("viewClass");
        const learnElements = document.getElementsByClassName("learnClass")
        const historyElements = document.getElementsByClassName("historyClass");
        function sendView(content) {
            window.location.href = "./schoolClass.html?class=" + content;
        }
        function sendLearn(content) {
            window.location.href = "./learnMode.html?class=" + content;
        }
        function sendHistory(content) {
            window.location.href = "./history.html?class=" + content;
        }
        for(var i = 0; i < viewElements.length; i++) {
            const value = viewElements[i].value
            viewElements[i].addEventListener('click', ()=> sendView(value));
            learnElements[i].addEventListener('click', ()=> sendLearn(value));
            historyElements[i].addEventListener('click', ()=> sendHistory(value));
        }
    })