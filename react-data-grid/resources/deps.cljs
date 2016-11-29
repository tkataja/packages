{:foreign-libs [{:file     "cljsjs/react-data-grid/development/react-data-grid.inc.js"
                 :provides ["cljsjs.react-data-grid"]
                 :file-min "cljsjs/react-data-grid/production/react-data-grid.min.inc.js"}
                {:file     "cljsjs/react-data-grid/development/react-data-grid.ui-plugins.inc.js"
                 :provides ["cljsjs.react-data-grid-ui-plugins"]
                 :requires ["cljsjs.react-data-grid"]
                 :file-min "cljsjs/react-data-grid/production/react-data-grid.ui-plugins.min.inc.js"}]
 :externs      ["cljsjs/react-data-grid/common/react-data-grid.ext.js"
                "cljsjs/react-data-grid/common/react-data-grid.ui-plugins.ext.js"]}
