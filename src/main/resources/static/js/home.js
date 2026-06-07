/* Home page motion — scroll reveals, condensing header, magnetic CTA.
   Scoped to the homepage (only loaded by home.html). Degrades gracefully:
   if anything is unsupported or the user prefers reduced motion, content
   is simply shown in its final state. */
(function () {
    'use strict';

    var reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    // ── Reveal elements as they enter the viewport ──────────────────────────
    var reveals = document.querySelectorAll('[data-reveal]');

    if (reduceMotion || !('IntersectionObserver' in window)) {
        reveals.forEach(function (el) { el.classList.add('is-visible'); });
    } else {
        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add('is-visible');
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.15, rootMargin: '0px 0px -8% 0px' });

        reveals.forEach(function (el) { observer.observe(el); });
    }

    // (Header condense-on-scroll is handled globally by site.js.)

    // ── Magnetic CTA (pointer devices only) ─────────────────────────────────
    var cta = document.querySelector('.hero-cta');
    if (cta && !reduceMotion && window.matchMedia('(hover: hover)').matches) {
        cta.addEventListener('mousemove', function (e) {
            var rect = cta.getBoundingClientRect();
            var x = (e.clientX - rect.left - rect.width / 2) * 0.3;
            var y = (e.clientY - rect.top - rect.height / 2) * 0.45;
            cta.style.transform = 'translate(' + x + 'px, ' + y + 'px)';
        });
        cta.addEventListener('mouseleave', function () {
            cta.style.transform = '';
        });
    }
})();
