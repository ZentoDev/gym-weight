function fetchBarbellSide() {
  fetch('/side?side=left') // o "right"
    .then(response => response.json())
    .then(data => {
      const output = document.getElementById('barbellSide');

      if (data.length === 0) {
        output.textContent = 'X';
      } else {
        output.textContent = data.join(' - ');
      }
    });
}

/* Ricevo un oggetto Map<Double, Integer> (weight, quantity) e lo formatto nel seguente formato "XX1 kg - YY1 | XX2 - YY | etc" */
function fetchAvailablePlates() {
	fetch('/available').then(response => response.json())
	  .then(data => {
		const output = document.getElementById('availablePlates');
		
		const entries = Object.entries(data);
		if (entries.length === 0) {
			output.textContent = `Nessun peso disponibile`;
		} else {
			const formatted = entries
			  .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0]))
			  .map(([weight, count]) => `${weight} kg - ${count}`)
			  .join(' | ');
			  
			output.textContent = formatted;
		}
	  });
}

// Chiamata al caricamento della pagina
window.onload = fetchBarbellSide;


function fetchCombination() {		
  const weight = document.getElementById("weightInput").value;
  
  fetch(`/combination?targetWeight=${weight}`).then(response => response.json())
    .then(data => {
      const list = document.getElementById("result");
      list.innerHTML = '';
	  
	  const li = document.createElement("li");
	  if (data.length === 0) {
		li.textContent = 'Nessuna operazione';
		list.appendChild(li);
	  } else {
		data.forEach(operation => {
		const li = document.createElement("li");
		li.textContent = `${operation.action} -> ${operation.weight} kg`;
		list.appendChild(li);
		});
	  }
    });
	setTimeout(() => {
		fetchBarbellSide()
		fetchAvailablePlates();   
    }, 100);
}


