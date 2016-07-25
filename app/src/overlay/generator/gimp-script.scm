(define background-color '(221 81 76))
(define fork-color '(0 0 0))

(define (process-image base-image
                       primary-color
                       secondary-color
                       dimension
                       path)
  (let* ((image (car (gimp-file-load RUN-NONINTERACTIVE base-image base-image)))
         (drawable (car (gimp-image-get-active-drawable image))))
    (paint-image image drawable background-color primary-color)
    (paint-image image drawable fork-color secondary-color)
    (scale-image dimension image)
    (write-png path image drawable)))

(define (paint-image image drawable from-color to-color)
    (gimp-image-select-color image CHANNEL-OP-REPLACE drawable from-color)
    (gimp-context-set-foreground to-color)
    (gimp-edit-bucket-fill-full drawable FG-BUCKET-FILL NORMAL-MODE 100 0 TRUE FALSE SELECT-CRITERION-COMPOSITE 0 0))

(define (scale-image dimension image)
  (gimp-image-scale image (car dimension) (car (cdr dimension))))

(define (write-png path image drawable)
  (file-png-save-defaults RUN-NONINTERACTIVE image drawable path path))
