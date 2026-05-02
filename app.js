(function () {
  const MODULES = [
    {
      id: "project_overview",
      name: "Project Overview",
      description: "Capture basic project context and high-level goals.",
      questions: [
        { id: "project_name", type: "text", prompt: "What is the project name?", required: true },
        { id: "business_goal", type: "text", prompt: "What is the primary business goal?", required: true }
      ]
    },
    {
      id: "service_cloud_routing",
      name: "Service Cloud Routing",
      description: "Define how work should be routed (queues, agents, or both).",
      questions: [
        {
          id: "routing_target",
          type: "single",
          prompt: "Should work be routed to queues, directly to agents, or both?",
          required: true,
          options: ["Queues only", "Agents directly", "Both queues and agents", "Not sure"]
        },
        {
          id: "queue_list",
          type: "text",
          prompt: "Which queues are needed? (comma-separated)",
          requiredIf: (answers) => ["Queues only", "Both queues and agents"].includes(answers.routing_target),
          visibleIf: (answers) => ["Queues only", "Both queues and agents"].includes(answers.routing_target),
          notApplicableReason: "Skipped because routing is not queue-based."
        },
        {
          id: "agent_capacity",
          type: "single",
          prompt: "Should each agent have a maximum concurrent workload?",
          options: ["Yes", "No", "Not sure"],
          requiredIf: (answers) => ["Agents directly", "Both queues and agents"].includes(answers.routing_target),
          visibleIf: (answers) => ["Agents directly", "Both queues and agents"].includes(answers.routing_target),
          notApplicableReason: "Skipped because the customer selected queue-based routing only."
        }
      ]
    },
    {
      id: "security_sharing",
      name: "Security and Sharing",
      description: "Capture security model and external access needs.",
      questions: [
        {
          id: "external_users",
          type: "single",
          prompt: "Do external users need access to Salesforce data?",
          required: true,
          options: ["Yes", "No", "Not sure"]
        },
        {
          id: "sso_required",
          type: "single",
          prompt: "Is SSO required?",
          options: ["Yes", "No", "Not sure"],
          required: true
        }
      ]
    }
  ];

  const state = {
    selectedModules: [],
    answers: {},
    questionStatus: {},
    needsReviewModules: new Set(),
    pointer: { moduleIndex: 0, questionIndex: 0 }
  };

  const moduleList = document.getElementById("module-list");
  const startWizardBtn = document.getElementById("start-wizard");
  const wizardEmpty = document.getElementById("wizard-empty");
  const wizard = document.getElementById("wizard");
  const moduleTitle = document.getElementById("module-title");
  const moduleDescription = document.getElementById("module-description");
  const moduleProgress = document.getElementById("module-progress");
  const questionContainer = document.getElementById("question-container");
  const prevQuestionBtn = document.getElementById("prev-question");
  const nextQuestionBtn = document.getElementById("next-question");
  const reviewSummary = document.getElementById("review-summary");
  const markReviewBtn = document.getElementById("mark-review");
  const exportJsonBtn = document.getElementById("export-json");
  const exportMdBtn = document.getElementById("export-md");

  function initModuleSelector() {
    moduleList.innerHTML = MODULES.map((m) => `
      <label class="checkbox-row">
        <input type="checkbox" value="${m.id}" />
        <span><strong>${m.name}</strong><br /><small>${m.description}</small></span>
      </label>
    `).join("");
  }

  function getSelectedModuleObjects() {
    return MODULES.filter((m) => state.selectedModules.includes(m.id));
  }

  function isVisible(question, answers) {
    return question.visibleIf ? question.visibleIf(answers) : true;
  }

  function isRequired(question, answers) {
    if (question.requiredIf) return question.requiredIf(answers);
    return Boolean(question.required);
  }

  function visibleQuestions(module) {
    return module.questions.filter((q) => isVisible(q, state.answers));
  }

  function renderQuestion() {
    const modules = getSelectedModuleObjects();
    const module = modules[state.pointer.moduleIndex];
    if (!module) return;

    const questions = visibleQuestions(module);
    if (questions.length === 0) {
      questionContainer.innerHTML = `<p class="muted">No questions apply in this module based on current answers.</p>`;
      return;
    }

    state.pointer.questionIndex = Math.min(state.pointer.questionIndex, questions.length - 1);
    const q = questions[state.pointer.questionIndex];

    moduleTitle.textContent = module.name;
    moduleDescription.textContent = module.description;
    moduleProgress.textContent = `Question ${state.pointer.questionIndex + 1} of ${questions.length}`;

    let inputHtml = "";
    const current = state.answers[q.id] || "";

    if (q.type === "text") {
      inputHtml = `<input id="question-input" type="text" value="${current}" />`;
    } else {
      inputHtml = q.options.map((option) => `
        <label class="radio-row">
          <input type="radio" name="question-input" value="${option}" ${current === option ? "checked" : ""} />
          <span>${option}</span>
        </label>
      `).join("");
    }

    questionContainer.innerHTML = `
      <div class="question-card">
        <p class="q-title">${q.prompt}</p>
        ${inputHtml}
      </div>
    `;

    prevQuestionBtn.disabled = state.pointer.moduleIndex === 0 && state.pointer.questionIndex === 0;
    nextQuestionBtn.textContent = isAtEnd() ? "Finish" : "Next";
  }

  function captureAnswer() {
    const modules = getSelectedModuleObjects();
    const module = modules[state.pointer.moduleIndex];
    const questions = visibleQuestions(module);
    const q = questions[state.pointer.questionIndex];
    if (!q) return true;

    let value = "";
    if (q.type === "text") {
      const input = document.getElementById("question-input");
      value = input ? input.value.trim() : "";
    } else {
      const selected = document.querySelector('input[name="question-input"]:checked');
      value = selected ? selected.value : "";
    }

    if (isRequired(q, state.answers) && !value) {
      state.questionStatus[q.id] = "missingRequired";
      alert("This is required before continuing.");
      return false;
    }

    state.answers[q.id] = value || null;
    state.questionStatus[q.id] = value ? "answered" : "notStarted";

    updateSkippedStatuses();
    updateReviewSummary();
    return true;
  }

  function updateSkippedStatuses() {
    const allQuestions = MODULES.flatMap((m) => m.questions);
    allQuestions.forEach((q) => {
      if (!isVisible(q, state.answers)) {
        state.questionStatus[q.id] = "notApplicable";
      }
    });
  }

  function isAtEnd() {
    const modules = getSelectedModuleObjects();
    const lastModuleIndex = modules.length - 1;
    const lastModuleQuestions = visibleQuestions(modules[lastModuleIndex] || { questions: [] });
    return state.pointer.moduleIndex === lastModuleIndex && state.pointer.questionIndex === lastModuleQuestions.length - 1;
  }

  function moveNext() {
    const modules = getSelectedModuleObjects();
    const module = modules[state.pointer.moduleIndex];
    const qCount = visibleQuestions(module).length;

    if (state.pointer.questionIndex < qCount - 1) {
      state.pointer.questionIndex += 1;
      return;
    }

    if (state.pointer.moduleIndex < modules.length - 1) {
      state.pointer.moduleIndex += 1;
      state.pointer.questionIndex = 0;
      return;
    }
  }

  function movePrev() {
    if (state.pointer.questionIndex > 0) {
      state.pointer.questionIndex -= 1;
      return;
    }

    if (state.pointer.moduleIndex > 0) {
      state.pointer.moduleIndex -= 1;
      const modules = getSelectedModuleObjects();
      const prevModule = modules[state.pointer.moduleIndex];
      state.pointer.questionIndex = Math.max(visibleQuestions(prevModule).length - 1, 0);
    }
  }

  function updateReviewSummary() {
    const selected = getSelectedModuleObjects();
    const answered = Object.values(state.questionStatus).filter((s) => s === "answered").length;
    const missing = Object.values(state.questionStatus).filter((s) => s === "missingRequired").length;
    const notApplicable = Object.values(state.questionStatus).filter((s) => s === "notApplicable").length;

    const moduleScores = selected.map((m) => {
      const visible = m.questions.filter((q) => isVisible(q, state.answers));
      const completed = visible.filter((q) => state.questionStatus[q.id] === "answered").length;
      const completionScore = visible.length ? Math.round((completed / visible.length) * 100) : 100;
      return `${m.name}: ${completionScore}%`;
    });

    reviewSummary.innerHTML = `
      <p><strong>Selected modules:</strong> ${selected.length}</p>
      <p><strong>Answered:</strong> ${answered}</p>
      <p><strong>Blocking gaps:</strong> ${missing}</p>
      <p><strong>Not applicable:</strong> ${notApplicable}</p>
      <p><strong>Needs architect review:</strong> ${Array.from(state.needsReviewModules).join(", ") || "None"}</p>
      <p><strong>Completeness:</strong><br />${moduleScores.join("<br />") || "N/A"}</p>
    `;
  }

  function exportPayload() {
    return {
      projectOverview: { projectName: state.answers.project_name || null },
      businessGoals: state.answers.business_goal ? [state.answers.business_goal] : [],
      selectedModules: state.selectedModules,
      answers: Object.entries(state.answers).map(([questionId, answer]) => ({ questionId, answer })),
      skippedQuestions: Object.entries(state.questionStatus)
        .filter(([, status]) => status === "notApplicable")
        .map(([questionId]) => ({ questionId, status: "notApplicable" })),
      needsConfirmation: Object.entries(state.answers)
        .filter(([, answer]) => answer === "Not sure")
        .map(([questionId, answer]) => ({ questionId, answer })),
      risks: state.answers.external_users === "Yes"
        ? ["External sharing model and Experience Cloud licensing impact must be reviewed."]
        : [],
      architectReviewItems: [
        ...Array.from(state.needsReviewModules).map((moduleId) => `Module marked as needs review: ${moduleId}`),
        ...(state.answers.sso_required === "Yes" ? ["Identity and SSO architecture review required."] : [])
      ]
    };
  }

  function downloadFile(filename, content, mimeType) {
    const blob = new Blob([content], { type: mimeType });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  }

  startWizardBtn.addEventListener("click", () => {
    state.selectedModules = Array.from(moduleList.querySelectorAll("input:checked")).map((input) => input.value);
    if (!state.selectedModules.length) {
      alert("Select at least one module.");
      return;
    }

    wizardEmpty.classList.add("hidden");
    wizard.classList.remove("hidden");
    state.pointer = { moduleIndex: 0, questionIndex: 0 };
    renderQuestion();
    updateReviewSummary();
  });

  nextQuestionBtn.addEventListener("click", () => {
    if (!captureAnswer()) return;
    moveNext();
    renderQuestion();
  });

  prevQuestionBtn.addEventListener("click", () => {
    movePrev();
    renderQuestion();
  });

  markReviewBtn.addEventListener("click", () => {
    const module = getSelectedModuleObjects()[state.pointer.moduleIndex];
    if (module) {
      state.needsReviewModules.add(module.id);
      updateReviewSummary();
    }
  });

  exportJsonBtn.addEventListener("click", () => {
    const payload = exportPayload();
    downloadFile("solution-discovery-v1.json", JSON.stringify(payload, null, 2), "application/json");
  });

  exportMdBtn.addEventListener("click", () => {
    const payload = exportPayload();
    const markdown = `# Salesforce Solution Discovery Summary\n\n## Business Requirements\n- ${payload.businessGoals.join("\n- ") || "Not provided"}\n\n## Functional Requirements\n- Routing target: ${state.answers.routing_target || "Not provided"}\n\n## Security Design\n- External users: ${state.answers.external_users || "Not provided"}\n- SSO required: ${state.answers.sso_required || "Not provided"}\n\n## Risks and Assumptions\n- ${payload.risks.join("\n- ") || "None identified"}\n\n## Architect Review Items\n- ${payload.architectReviewItems.join("\n- ") || "None"}\n`;
    downloadFile("solution-discovery-v1.md", markdown, "text/markdown");
  });

  initModuleSelector();
})();
