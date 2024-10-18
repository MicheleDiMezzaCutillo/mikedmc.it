// Login Script
const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.querySelector('.container');

signUpButton.addEventListener('click', () => {
	container.classList.add('right-panel-active');
});

signInButton.addEventListener('click', () => {
	container.classList.remove('right-panel-active');
});

function Strength(password) {
  let i = 0;
  if (password.length > 6) {
    i++;
  }
  if (password.length >= 10) {
    i++;
  }

  if (/[A-Z]/.test(password)) {
    i++;
  }

  if (/[0-9]/.test(password)) {
    i++;
  }

  if (/[A-Za-z0-8]/.test(password)) {
    i++;
  }

  return i;
}

let strengthCheker = document.querySelector(".passColor");
document.addEventListener("keyup", function (e) {
  let password = document.querySelector(".password-checker").value;

  let strength = Strength(password);
  if (strength <= 2) {
    strengthCheker.classList.add("weak");
    strengthCheker.classList.remove("moderate");
    strengthCheker.classList.remove("strong");
  } else if (strength >= 2 && strength <= 4) {
    strengthCheker.classList.remove("weak");
    strengthCheker.classList.add("moderate");
    strengthCheker.classList.remove("strong");
  } else {
    strengthCheker.classList.remove("weak");
    strengthCheker.classList.remove("moderate");
    strengthCheker.classList.add("strong");
  }
});