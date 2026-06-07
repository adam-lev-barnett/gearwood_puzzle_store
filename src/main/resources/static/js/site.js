/* Site-wide UI behavior, loaded on every page via MainLayout.
   Condenses the sticky header once the user scrolls past the top. */
(function () {
    'use strict';

    var header = document.querySelector('.main-header');
    if (!header) return;

    var onScroll = function () {
        header.classList.toggle('scrolled', window.scrollY > 24);
    };

    window.addEventListener('scroll', onScroll, { passive: true });
    onScroll();
})();
