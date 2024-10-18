document.querySelectorAll('.mItem img').forEach(image => {
    image.addEventListener('click', function() {
        const modal = document.getElementById('image-modal');
        const modalImage = document.getElementById('modal-image');
        const modalTitle = document.getElementById('modal-title');
        const modalDescription = document.getElementById('modal-description');

        // Set the source of the modal image
        modalImage.src = this.src;

        // Set the title and description from data attributes
        modalTitle.textContent = this.getAttribute('data-title') || "";
        modalDescription.textContent = this.getAttribute('data-description') || "";

        // Show the modal
        modal.style.display = 'flex';
    });
});

// Close the modal when clicking outside of the content
document.getElementById('image-modal').addEventListener('click', function(e) {
        this.style.display = 'none';
});
