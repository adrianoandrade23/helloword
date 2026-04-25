(function () {
  const form = document.getElementById("waitlist-form");
  const emailInput = document.getElementById("email");
  const feedback = document.getElementById("form-feedback");
  const submitButton = document.getElementById("waitlist-submit");

  const config = window.APP_CONFIG || {};

  function setFeedback(message, type) {
    feedback.textContent = message;
    feedback.classList.remove("success", "error", "info");
    feedback.classList.add(type);
  }

  async function createWaitlistSignup(email) {
    const url = config.supabaseUrl;
    const key = config.supabaseAnonKey;
    const table = config.waitlistTable || "waitlist_signups";

    if (!url || !key) {
      throw new Error("Waitlist persistence is not configured yet.");
    }

    const endpoint = `${url.replace(/\/$/, "")}/rest/v1/${table}`;
    const response = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        apikey: key,
        Authorization: `Bearer ${key}`,
        Prefer: "return=minimal",
      },
      body: JSON.stringify({
        email,
        source: "landing-page",
      }),
    });

    if (!response.ok) {
      const errorBody = await response.text();
      throw new Error(errorBody || "Unable to save your signup.");
    }
  }

  form.addEventListener("submit", async function (event) {
    event.preventDefault();
    const email = emailInput.value.trim().toLowerCase();

    if (!email) {
      setFeedback("Please enter an email address.", "error");
      return;
    }

    submitButton.disabled = true;
    submitButton.textContent = "Saving...";
    setFeedback("Saving your signup...", "info");

    try {
      await createWaitlistSignup(email);
      form.reset();
      setFeedback("Thanks! You're on the waitlist.", "success");
    } catch (error) {
      console.error(error);
      setFeedback("Could not save signup right now. Please try again shortly.", "error");
    } finally {
      submitButton.disabled = false;
      submitButton.textContent = "Notify me";
    }
  });
})();
