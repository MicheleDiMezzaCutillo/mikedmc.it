document.addEventListener("DOMContentLoaded", async function () {
            const contentArea = document.getElementById("doc-content-area");
            const code = /*[[${project.code}]]*/ 'project-code'; // FallBack statico se Thymeleaf non risolve

            try {
                const response = await fetch(`/api/documentation/${code}/contents`);
                if (!response.ok) throw new Error("Errore nel caricamento dei contenuti");

                const contents = await response.json();
                contentArea.innerHTML = ""; // pulisce il messaggio "Caricamento..."
                console.log(Array.isArray(contents), contents);

                contents.forEach(item => {
                    let element = document.createElement("div");
                    element.className = "p-4 bg-white rounded shadow";

                    switch (item.type) {
                        case "H1":
                            element.innerHTML = `<h1 class="text-3xl font-bold">${item.data}</h1>`;
                            break;
                        case "H2":
                            element.innerHTML = `<h2 class="text-2xl font-semibold">${item.data}</h2>`;
                            break;
                        case "P":
                            element.innerHTML = `<p class="text-gray-700">${item.data}</p>`;
                            break;
                        case "CODE":
                            element.innerHTML = `<pre class="bg-gray-100 p-2 rounded"><code>${item.data}</code></pre>`;
                            break;
                        default:
                            element.innerHTML = `<p>${item.data}</p>`;
                    }

                    contentArea.appendChild(element);
                });

            } catch (e) {
                contentArea.innerHTML = `<p class="text-red-500">Errore nel caricamento: ${e.message}</p>`;
            }
        });