document.addEventListener("DOMContentLoaded", () => {

    const rows = document.querySelectorAll(".manga-row");

    rows.forEach((row) => {

        let isDragging = false;
        let startX = 0;
        let startScrollLeft = 0;
        let dragged = false;

        // =========================
        // CREATE ARROW WRAPPER
        // =========================

        const wrapper = document.createElement("div");
        wrapper.classList.add("manga-scroll-wrapper");

        row.parentNode.insertBefore(wrapper, row);
        wrapper.appendChild(row);

        // =========================
        // LEFT BUTTON
        // =========================

        const leftButton = document.createElement("button");

        leftButton.classList.add(
            "scroll-arrow",
            "scroll-arrow-left"
        );

        leftButton.innerHTML =
            '<i class="fa-solid fa-chevron-left"></i>';

        wrapper.appendChild(leftButton);

        // =========================
        // RIGHT BUTTON
        // =========================

        const rightButton = document.createElement("button");

        rightButton.classList.add(
            "scroll-arrow",
            "scroll-arrow-right"
        );

        rightButton.innerHTML =
            '<i class="fa-solid fa-chevron-right"></i>';

        wrapper.appendChild(rightButton);


        // =========================
        // ARROW SCROLL
        // =========================

        const getScrollAmount = () => {

            const card = row.querySelector(".manga-card");

            if (!card) {
                return 500;
            }

            const cardWidth =
                card.getBoundingClientRect().width;

            return (cardWidth + 18) * 4;
        };


        leftButton.addEventListener("click", () => {

            row.scrollBy({
                left: -getScrollAmount(),
                behavior: "smooth"
            });

        });


        rightButton.addEventListener("click", () => {

            row.scrollBy({
                left: getScrollAmount(),
                behavior: "smooth"
            });

        });


        // =========================
        // MOUSE WHEEL
        // =========================

        row.addEventListener(
            "wheel",
            (event) => {

                const canScroll =
                    row.scrollWidth > row.clientWidth;

                if (!canScroll) {
                    return;
                }

                const atStart =
                    row.scrollLeft <= 0;

                const atEnd =
                    Math.ceil(
                        row.scrollLeft + row.clientWidth
                    ) >= row.scrollWidth;


                /*
                 Allow normal page scrolling when:
                 user reaches horizontal beginning/end.
                */

                if (
                    (event.deltaY < 0 && atStart) ||
                    (event.deltaY > 0 && atEnd)
                ) {
                    return;
                }


                event.preventDefault();

                row.scrollBy({
                    left: event.deltaY * 1.4,
                    behavior: "auto"
                });

            },
            {
                passive: false
            }
        );


        // =========================
        // CLICK + DRAG
        // =========================

        row.addEventListener(
            "mousedown",
            (event) => {

                isDragging = true;

                dragged = false;

                startX = event.pageX;

                startScrollLeft =
                    row.scrollLeft;

                row.classList.add("dragging");

            }
        );


        window.addEventListener(
            "mousemove",
            (event) => {

                if (!isDragging) {
                    return;
                }

                const distance =
                    event.pageX - startX;

                if (Math.abs(distance) > 5) {
                    dragged = true;
                }

                row.scrollLeft =
                    startScrollLeft - distance;

            }
        );


        window.addEventListener(
            "mouseup",
            () => {

                if (!isDragging) {
                    return;
                }

                isDragging = false;

                row.classList.remove("dragging");

            }
        );


        // =========================
        // PREVENT LINK CLICK
        // AFTER DRAGGING
        // =========================

        row.addEventListener(
            "click",
            (event) => {

                if (dragged) {

                    event.preventDefault();

                    event.stopPropagation();

                    dragged = false;
                }

            },
            true
        );


        // =========================
        // SHOW/HIDE ARROWS
        // =========================

        const updateArrows = () => {

            const maxScroll =
                row.scrollWidth -
                row.clientWidth;

            if (row.scrollLeft <= 2) {

                leftButton.classList.add(
                    "arrow-hidden"
                );

            } else {

                leftButton.classList.remove(
                    "arrow-hidden"
                );
            }


            if (
                row.scrollLeft >=
                maxScroll - 2
            ) {

                rightButton.classList.add(
                    "arrow-hidden"
                );

            } else {

                rightButton.classList.remove(
                    "arrow-hidden"
                );
            }


            if (
                row.scrollWidth <=
                row.clientWidth
            ) {

                leftButton.classList.add(
                    "arrow-hidden"
                );

                rightButton.classList.add(
                    "arrow-hidden"
                );
            }

        };


        row.addEventListener(
            "scroll",
            updateArrows
        );

        window.addEventListener(
            "resize",
            updateArrows
        );

        updateArrows();

    });

});