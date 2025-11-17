const API_BASE = "http://localhost:8080";

// ===================== TEMA (CLARO / ESCURO) =====================

const themeToggleBtn = document.getElementById("theme-toggle");

function applyTheme(theme) {
    const body = document.body;
    body.setAttribute("data-theme", theme);
    if (theme === "dark") {
        themeToggleBtn.textContent = "☀️ Claro";
    } else {
        themeToggleBtn.textContent = "🌙 Escuro";
    }
}

function loadInitialTheme() {
    const saved = localStorage.getItem("theme");
    const initial = saved || "dark"; // default: escuro
    applyTheme(initial);
}

if (themeToggleBtn) {
    themeToggleBtn.addEventListener("click", () => {
        const current = document.body.getAttribute("data-theme") || "dark";
        const next = current === "dark" ? "light" : "dark";
        applyTheme(next);
        localStorage.setItem("theme", next);
    });
}

// ===================== UTIL =====================

function handleResponse(response) {
    if (!response.ok) {
        return response.text().then(text => {
            throw new Error(text || "Erro na requisição");
        });
    }
    if (response.status === 204) {
        return null;
    }
    return response.json();
}

function formatCurrency(value) {
    if (value == null) return "0,00";
    return value.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function formatDate(dateStr) {
    if (!dateStr) return "";
    return dateStr.substring(0, 10).split("-").reverse().join("/");
}

// ===================== TABS =====================

document.querySelectorAll(".tab-button").forEach(btn => {
    btn.addEventListener("click", () => {
        document.querySelectorAll(".tab-button").forEach(b => b.classList.remove("active"));
        document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));

        btn.classList.add("active");
        const tabId = btn.getAttribute("data-tab");
        document.getElementById(tabId).classList.add("active");
    });
});

// ===================== MÁQUINAS =====================

const tabelaMaquinasBody = document.getElementById("tabela-maquinas-body");
const formMaquina = document.getElementById("form-maquina");
const btnMaquinaLimpar = document.getElementById("btn-maquina-limpar");
const listaAlertas = document.getElementById("lista-alertas");

function carregarMaquinas() {
    fetch(`${API_BASE}/api/maquinas`)
        .then(handleResponse)
        .then(maquinas => {
            tabelaMaquinasBody.innerHTML = "";
            maquinas.forEach(m => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${m.id}</td>
                    <td>${m.nome}</td>
                    <td>${m.tipo || ""}</td>
                    <td>${m.modelo || ""}</td>
                    <td>${m.horimetroAtual ?? "-"}</td>
                    <td>
                        <button data-id="${m.id}" class="btn-editar-maquina">Editar</button>
                        <button data-id="${m.id}" class="btn-excluir-maquina">Excluir</button>
                    </td>
                `;
                tabelaMaquinasBody.appendChild(tr);
            });

            preencherSelectMaquinas("select-maquina-manutencao", maquinas);
            preencherSelectMaquinas("select-maquina-abastecimento", maquinas);
            preencherSelectMaquinas("select-maquina-horas", maquinas);
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar máquinas");
        });
}

function preencherSelectMaquinas(selectId, maquinas) {
    const select = document.getElementById(selectId);
    if (!select) return;
    select.innerHTML = "";
    maquinas.forEach(m => {
        const opt = document.createElement("option");
        opt.value = m.id;
        opt.textContent = `${m.id} - ${m.nome}`;
        select.appendChild(opt);
    });

    if (selectId === "select-maquina-manutencao") {
        carregarManutencoes();
        atualizarTotalManutencao();
    }
    if (selectId === "select-maquina-abastecimento") {
        carregarAbastecimentos();
        atualizarResumoAbastecimento();
    }
    if (selectId === "select-maquina-horas") {
        carregarHoras();
        atualizarTotalHoras();
    }
}

formMaquina.addEventListener("submit", (e) => {
    e.preventDefault();

    const id = document.getElementById("maquina-id").value || null;
    const body = {
        nome: document.getElementById("maquina-nome").value,
        tipo: document.getElementById("maquina-tipo").value,
        modelo: document.getElementById("maquina-modelo").value || null,
        ano: document.getElementById("maquina-ano").value ? parseInt(document.getElementById("maquina-ano").value) : null,
        numeroSerie: document.getElementById("maquina-numero-serie").value || null,
        horimetroAtual: document.getElementById("maquina-horimetro-atual").value ? parseFloat(document.getElementById("maquina-horimetro-atual").value) : null,
        horimetroProxRevisao: document.getElementById("maquina-horimetro-prox-revisao").value ? parseFloat(document.getElementById("maquina-horimetro-prox-revisao").value) : null,
        dataUltimaManutencao: document.getElementById("maquina-data-ultima-manutencao").value || null,
        dataProxManutencao: document.getElementById("maquina-data-prox-manutencao").value || null,
        ativo: document.getElementById("maquina-ativo").checked
    };

    const method = id ? "PUT" : "POST";
    const url = id ? `${API_BASE}/api/maquinas/${id}` : `${API_BASE}/api/maquinas`;

    fetch(url, {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })
        .then(handleResponse)
        .then(() => {
            alert("Máquina salva com sucesso!");
            limparFormularioMaquina();
            carregarMaquinas();
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao salvar máquina");
        });
});

btnMaquinaLimpar.addEventListener("click", () => {
    limparFormularioMaquina();
});

function limparFormularioMaquina() {
    document.getElementById("maquina-id").value = "";
    formMaquina.reset();
    document.getElementById("maquina-ativo").checked = true;
}

tabelaMaquinasBody.addEventListener("click", (e) => {
    const id = e.target.getAttribute("data-id");
    if (!id) return;

    if (e.target.classList.contains("btn-editar-maquina")) {
        editarMaquina(id);
    } else if (e.target.classList.contains("btn-excluir-maquina")) {
        if (confirm("Deseja realmente excluir esta máquina?")) {
            excluirMaquina(id);
        }
    }
});

function editarMaquina(id) {
    fetch(`${API_BASE}/api/maquinas/${id}`)
        .then(handleResponse)
        .then(m => {
            document.getElementById("maquina-id").value = m.id;
            document.getElementById("maquina-nome").value = m.nome || "";
            document.getElementById("maquina-tipo").value = m.tipo || "";
            document.getElementById("maquina-modelo").value = m.modelo || "";
            document.getElementById("maquina-ano").value = m.ano || "";
            document.getElementById("maquina-numero-serie").value = m.numeroSerie || "";
            document.getElementById("maquina-horimetro-atual").value = m.horimetroAtual ?? "";
            document.getElementById("maquina-horimetro-prox-revisao").value = m.horimetroProxRevisao ?? "";
            document.getElementById("maquina-data-ultima-manutencao").value = m.dataUltimaManutencao || "";
            document.getElementById("maquina-data-prox-manutencao").value = m.dataProxManutencao || "";
            document.getElementById("maquina-ativo").checked = m.ativo ?? true;
            window.scrollTo({ top: 0, behavior: "smooth" });
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar dados da máquina");
        });
}

function excluirMaquina(id) {
    fetch(`${API_BASE}/api/maquinas/${id}`, { method: "DELETE" })
        .then(handleResponse)
        .then(() => {
            alert("Máquina excluída");
            carregarMaquinas();
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao excluir máquina");
        });
}

document.getElementById("btn-buscar-alertas-horas")
    .addEventListener("click", () => {
        const margem = document.getElementById("alerta-margem-horas").value || 10;
        fetch(`${API_BASE}/api/maquinas/alertas/horas?margemHoras=${margem}`)
            .then(handleResponse)
            .then(maquinas => {
                listaAlertas.innerHTML = "";
                if (maquinas.length === 0) {
                    listaAlertas.innerHTML = "<li>Nenhuma máquina próxima da revisão por horas.</li>";
                } else {
                    maquinas.forEach(m => {
                        const li = document.createElement("li");
                        li.textContent = `Máquina ${m.id} - ${m.nome} (Horímetro: ${m.horimetroAtual}, Próx: ${m.horimetroProxRevisao})`;
                        listaAlertas.appendChild(li);
                    });
                }
            })
            .catch(err => {
                console.error(err);
                alert("Erro ao buscar alertas");
            });
    });

document.getElementById("btn-buscar-alertas-data")
    .addEventListener("click", () => {
        const dias = document.getElementById("alerta-dias-antes").value || 7;
        fetch(`${API_BASE}/api/maquinas/alertas/datas?diasAntes=${dias}`)
            .then(handleResponse)
            .then(maquinas => {
                listaAlertas.innerHTML = "";
                if (maquinas.length === 0) {
                    listaAlertas.innerHTML = "<li>Nenhuma máquina próxima da revisão por data.</li>";
                } else {
                    maquinas.forEach(m => {
                        const li = document.createElement("li");
                        li.textContent = `Máquina ${m.id} - ${m.nome} (Próx. Manutenção: ${formatDate(m.dataProxManutencao)})`;
                        listaAlertas.appendChild(li);
                    });
                }
            })
            .catch(err => {
                console.error(err);
                alert("Erro ao buscar alertas");
            });
    });

// ===================== MANUTENÇÕES =====================

const selectMaquinaManutencao = document.getElementById("select-maquina-manutencao");
const tabelaManutencoesBody = document.getElementById("tabela-manutencoes-body");
const formManutencao = document.getElementById("form-manutencao");
const totalManutencaoSpan = document.getElementById("total-manutencao");
const btnAtualizarTotalManutencao = document.getElementById("btn-atualizar-total-manutencao");

selectMaquinaManutencao.addEventListener("change", () => {
    carregarManutencoes();
    atualizarTotalManutencao();
});

formManutencao.addEventListener("submit", (e) => {
    e.preventDefault();
    const maquinaId = selectMaquinaManutencao.value;
    if (!maquinaId) {
        alert("Selecione uma máquina");
        return;
    }

    const body = {
        data: document.getElementById("manutencao-data").value,
        tipo: document.getElementById("manutencao-tipo").value,
        descricao: document.getElementById("manutencao-descricao").value || null,
        custo: parseFloat(document.getElementById("manutencao-custo").value),
        horasUsoNoMomento: document.getElementById("manutencao-horas-uso").value
            ? parseFloat(document.getElementById("manutencao-horas-uso").value)
            : null
    };

    fetch(`${API_BASE}/api/manutencoes/maquina/${maquinaId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    })
        .then(handleResponse)
        .then(() => {
            alert("Manutenção registrado");
            formManutencao.reset();
            carregarManutencoes();
            atualizarTotalManutencao();
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao registrar manutenção");
        });
});

function carregarManutencoes() {
    const maquinaId = selectMaquinaManutencao.value;
    if (!maquinaId) return;

    fetch(`${API_BASE}/api/manutencoes/maquina/${maquinaId}`)
        .then(handleResponse)
        .then(manutencoes => {
            tabelaManutencoesBody.innerHTML = "";
            manutencoes.forEach(m => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${formatDate(m.data)}</td>
                    <td>${m.tipo || ""}</td>
                    <td>${m.descricao || ""}</td>
                    <td>R$ ${formatCurrency(m.custo)}</td>
                    <td>${m.horasUsoNoMomento ?? "-"}</td>
                `;
                tabelaManutencoesBody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar manutenções");
        });
}

function atualizarTotalManutencao() {
    const maquinaId = selectMaquinaManutencao.value;
    if (!maquinaId) return;

    fetch(`${API_BASE}/api/manutencoes/maquina/${maquinaId}/total-gasto`)
        .then(handleResponse)
        .then(total => {
            totalManutencaoSpan.textContent = formatCurrency(total);
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao obter total de manutenção");
        });
}

btnAtualizarTotalManutencao.addEventListener("click", atualizarTotalManutencao);

// ===================== ABASTECIMENTOS =====================

const selectMaquinaAbastecimento = document.getElementById("select-maquina-abastecimento");
const tabelaAbastecimentosBody = document.getElementById("tabela-abastecimentos-body");
const formAbastecimento = document.getElementById("form-abastecimento");
const resumoLitrosSpan = document.getElementById("resumo-litros");
const resumoCustoSpan = document.getElementById("resumo-custo");
const resumoConsumoMedioSpan = document.getElementById("resumo-consumo-medio");
const btnAtualizarResumo = document.getElementById("btn-atualizar-resumo");

selectMaquinaAbastecimento.addEventListener("change", () => {
    carregarAbastecimentos();
    atualizarResumoAbastecimento();
});

formAbastecimento.addEventListener("submit", (e) => {
    e.preventDefault();
    const maquinaId = selectMaquinaAbastecimento.value;
    if (!maquinaId) {
        alert("Selecione uma máquina");
        return;
    }

    const body = {
        data: document.getElementById("abastecimento-data").value,
        litros: parseFloat(document.getElementById("abastecimento-litros").value),
        custoTotal: parseFloat(document.getElementById("abastecimento-custo").value),
        horimetro: document.getElementById("abastecimento-horimetro").value
            ? parseFloat(document.getElementById("abastecimento-horimetro").value)
            : null
    };

    fetch(`${API_BASE}/api/abastecimentos/maquina/${maquinaId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    })
        .then(handleResponse)
        .then(() => {
            alert("Abastecimento registrado");
            formAbastecimento.reset();
            carregarAbastecimentos();
            atualizarResumoAbastecimento();
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao registrar abastecimento");
        });
});

function carregarAbastecimentos() {
    const maquinaId = selectMaquinaAbastecimento.value;
    if (!maquinaId) return;

    fetch(`${API_BASE}/api/abastecimentos/maquina/${maquinaId}`)
        .then(handleResponse)
        .then(abastecimentos => {
            tabelaAbastecimentosBody.innerHTML = "";
            abastecimentos.forEach(a => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${formatDate(a.data)}</td>
                    <td>${a.litros ?? "-"}</td>
                    <td>R$ ${formatCurrency(a.custoTotal)}</td>
                    <td>${a.horimetro ?? "-"}</td>
                `;
                tabelaAbastecimentosBody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar abastecimentos");
        });
}

function atualizarResumoAbastecimento() {
    const maquinaId = selectMaquinaAbastecimento.value;
    if (!maquinaId) return;

    const dataInicio = document.getElementById("resumo-data-inicio").value;
    const dataFim = document.getElementById("resumo-data-fim").value;

    const params = new URLSearchParams();
    if (dataInicio) params.append("dataInicio", dataInicio);
    if (dataFim) params.append("dataFim", dataFim);

    fetch(`${API_BASE}/api/abastecimentos/maquina/${maquinaId}/totais${params.toString() ? "?" + params.toString() : ""}`)
        .then(handleResponse)
        .then(res => {
            resumoLitrosSpan.textContent = res.litros ?? 0;
            resumoCustoSpan.textContent = formatCurrency(res.custoTotal);
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao obter totais de abastecimento");
        });

    fetch(`${API_BASE}/api/abastecimentos/maquina/${maquinaId}/consumo-medio${params.toString() ? "?" + params.toString() : ""}`)
        .then(handleResponse)
        .then(consumo => {
            if (consumo == null) {
                resumoConsumoMedioSpan.textContent = "sem dados";
            } else {
                resumoConsumoMedioSpan.textContent = consumo.toFixed(2);
            }
        })
        .catch(err => {
            console.error(err);
            resumoConsumoMedioSpan.textContent = "erro";
        });
}

btnAtualizarResumo.addEventListener("click", atualizarResumoAbastecimento);

// ===================== REGISTRO DE HORAS =====================

const selectMaquinaHoras = document.getElementById("select-maquina-horas");
const formHoras = document.getElementById("form-horas");
const tabelaHorasBody = document.getElementById("tabela-horas-body");
const horasDataInicioInput = document.getElementById("horas-data-inicio");
const horasDataFimInput = document.getElementById("horas-data-fim");
const btnFiltrarHoras = document.getElementById("btn-filtrar-horas");
const totalHorasPeriodoSpan = document.getElementById("total-horas-periodo");
const btnAtualizarTotalHoras = document.getElementById("btn-atualizar-total-horas");

if (selectMaquinaHoras) {
    selectMaquinaHoras.addEventListener("change", () => {
        carregarHoras();
        atualizarTotalHoras();
    });
}

if (formHoras) {
    formHoras.addEventListener("submit", (e) => {
        e.preventDefault();
        const maquinaId = selectMaquinaHoras.value;
        if (!maquinaId) {
            alert("Selecione uma máquina");
            return;
        }

        const body = {
            data: document.getElementById("horas-data").value,
            horasTrabalhadas: parseFloat(document.getElementById("horas-quantidade").value),
            observacao: document.getElementById("horas-observacao").value || null
        };

        fetch(`${API_BASE}/api/horas/maquina/${maquinaId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        })
            .then(handleResponse)
            .then(() => {
                alert("Horas registradas");
                formHoras.reset();
                carregarHoras();
                atualizarTotalHoras();
            })
            .catch(err => {
                console.error(err);
                alert("Erro ao registrar horas");
            });
    });
}

if (btnFiltrarHoras) {
    btnFiltrarHoras.addEventListener("click", () => {
        carregarHoras();
        atualizarTotalHoras();
    });
}

if (btnAtualizarTotalHoras) {
    btnAtualizarTotalHoras.addEventListener("click", atualizarTotalHoras);
}

function carregarHoras() {
    const maquinaId = selectMaquinaHoras.value;
    if (!maquinaId) return;

    const params = new URLSearchParams();
    const dataInicio = horasDataInicioInput.value;
    const dataFim = horasDataFimInput.value;
    if (dataInicio) params.append("dataInicio", dataInicio);
    if (dataFim) params.append("dataFim", dataFim);

    const url = `${API_BASE}/api/horas/maquina/${maquinaId}${params.toString() ? "?" + params.toString() : ""}`;

    fetch(url)
        .then(handleResponse)
        .then(registros => {
            tabelaHorasBody.innerHTML = "";
            registros.forEach(r => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${formatDate(r.data)}</td>
                    <td>${r.horasTrabalhadas ?? "-"}</td>
                    <td>${r.observacao || ""}</td>
                `;
                tabelaHorasBody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao carregar registros de horas");
        });
}

function atualizarTotalHoras() {
    const maquinaId = selectMaquinaHoras.value;
    if (!maquinaId) return;

    const params = new URLSearchParams();
    const dataInicio = horasDataInicioInput.value;
    const dataFim = horasDataFimInput.value;
    if (dataInicio) params.append("dataInicio", dataInicio);
    if (dataFim) params.append("dataFim", dataFim);

    const url = `${API_BASE}/api/horas/maquina/${maquinaId}/total${params.toString() ? "?" + params.toString() : ""}`;

    fetch(url)
        .then(handleResponse)
        .then(total => {
            totalHorasPeriodoSpan.textContent = (total ?? 0).toFixed(2);
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao obter total de horas");
        });
}

// ===================== INICIALIZAÇÃO =====================

window.addEventListener("DOMContentLoaded", () => {
    loadInitialTheme();
    carregarMaquinas();
});
