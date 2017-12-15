/*! Angular-PDF Version: 0.3.9 | (C) Sayanee Basu 2014, released under an MIT license */
(function() {

  'use strict';

  angular.module('pdf', []).directive('ngPdf', [ '$window', function($window) {
    return {
      restrict: 'E',
      templateUrl: function(element, attr) {
        return attr.templateUrl ? attr.templateUrl : 'partials/viewer.html'
      },
      link: function(scope, element, attrs) {
        var url = scope.pdfUrl,
          pdfDoc = null,
          pageNum = 1,
          scale = (attrs.scale ? attrs.scale : 1),
          canvas = (attrs.canvasid ? document.getElementById(attrs.canvasid) : document.getElementById('pdf-canvas')),
          ctx = canvas.getContext('2d'),
          windowEl = angular.element($window),
          maxZoomIn = attrs.maxzoomin ? attrs.maxzoomin:1.5,
          minZoomOut = attrs.minzoomout ? attrs.minzoomout:0;
        windowEl.on('scroll', function() {
          scope.$apply(function() {
            scope.scroll = windowEl[0].scrollY;
          });
        });

        PDFJS.disableWorker = true;
        scope.pageNum = pageNum;

        scope.renderPage = function(num) {
          pdfDoc.getPage(num).then(function(page) {
            var viewport = page.getViewport(scale),
              renderContext = {};

            canvas.height = viewport.height;
            canvas.width = viewport.width;

            renderContext = {
              canvasContext: ctx,
              viewport: viewport
            };

            page.render(renderContext);
          });
        };
        
        scope.goDownload = function (){
        	var strFileName = scope.pdfName;
        	var strMimeType="application/pdf";
        	var D = document, A = arguments;
			var a = D.createElement("a"), n = A[1];
			a.href = window.URL.createObjectURL(new Blob([url], { type: strMimeType }));
			if ('download' in a) {
				a.setAttribute("download", n);
				a.innerHTML = "downloading...";
				a.download = strFileName;
				D.body.appendChild(a);
				setTimeout(function() {
					var e = D.createEvent("MouseEvents");
					e.initMouseEvent("click", true, false, window, 0, 0, 0, 0,
							0, false, false, false, false, 0, null);
					a.dispatchEvent(e);
					D.body.removeChild(a);
				}, 66);
				return true;
			}
        };

        scope.goPrevious = function() {
          if (scope.pageToDisplay <= 1) {
            return;
          }
          scope.pageNum = parseInt(scope.pageNum) - 1;
        };

        scope.goNext = function() {
          if (scope.pageToDisplay >= pdfDoc.numPages) {
            return;
          }
          scope.pageNum = parseInt(scope.pageNum) + 1;
        };

        scope.zoomIn = function() {
          var auxScale = parseFloat(scale) + 0.2;
          if(auxScale<parseFloat(maxZoomIn)){
               scale = auxScale;
               scope.renderPage(scope.pageToDisplay);  
          }
          return scale; 
        };

        scope.zoomOut = function() {
            var auxScale = parseFloat(scale) - 0.2;
            if(auxScale>parseFloat(minZoomOut)){
                 scale = auxScale;
                 scope.renderPage(scope.pageToDisplay);  
            }	
            return scale;
        };

        scope.changePage = function() {
          scope.renderPage(scope.pageToDisplay);
        };

        scope.rotate = function() {
          if (canvas.getAttribute('class') === 'rotate0') {
            canvas.setAttribute('class', 'rotate90');
          } else if (canvas.getAttribute('class') === 'rotate90') {
            canvas.setAttribute('class', 'rotate180');
          } else if (canvas.getAttribute('class') === 'rotate180') {
            canvas.setAttribute('class', 'rotate270');
          } else {
            canvas.setAttribute('class', 'rotate0');
          }
        };

        PDFJS.getDocument(url, null, null, scope.onProgress).then(
          function(_pdfDoc) {
            if (typeof scope.onLoad === 'function' ) {
              scope.onLoad();
            }

            pdfDoc = _pdfDoc;
            scope.renderPage(scope.pageToDisplay);

            scope.$apply(function() {
              scope.pageCount = _pdfDoc.numPages;
            });
          }, function(error) {
            if (error) {
              if (typeof scope.onError === 'function') {
                scope.onError(error);
              }
            }
          }
        );

        scope.$watch('pageNum', function(newVal) {
          scope.pageToDisplay = parseInt(newVal);
          if (pdfDoc !== null) {
            scope.renderPage(scope.pageToDisplay);
          }
        });

      }
    };
  } ]);

})();
