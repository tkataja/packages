(set-env!
 :resource-paths #{"resources"}
 :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"] ])

(require '[cljsjs.boot-cljsjs.packaging :refer :all]
         '[boot.core :as boot]
         '[boot.tmpdir :as tmpd]
         '[clojure.java.io :as io]
         '[boot.util :refer [sh]])

(def +lib-version+ "1.0.65")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom  {:project     'cljsjs/react-data-grid
       :version     +version+
       :description "Excel-like grid component built with React"
       :url         "http://adazzle.github.io/react-data-grid/"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"CC BY-NC" "http://creativecommons.org/licenses/by-nc/3.0/"}})

(deftask build-react-data-grid []
  (let [tmp (boot/tmp-dir!)]
    (with-pre-wrap
      fileset
      (doseq [f (->> fileset boot/input-files)
              :let [target (io/file tmp (tmpd/path f))]]
        (io/make-parents target)
        (io/copy (tmpd/file f) target))
      (binding [boot.util/*sh-dir* (str (io/file tmp (format "react-data-grid-%s" +lib-version+)))]
        ((sh "npm" "install")))
      (-> fileset (boot/add-resource tmp) boot/commit!))))

(deftask package []
  (comp
   (download :url      (format "https://github.com/adazzle/react-data-grid/archive/v%s.zip" +lib-version+)
             :checksum "70A08DE961056EBD763466223836A3B4"
             :unzip    true)
   (build-react-data-grid)
   (sift      :move     {#"^react-data-grid.*/dist/react-data-grid\.js$"                  "cljsjs/react-data-grid/development/react-data-grid.inc.js"
                         #"^react-data-grid.*/dist/react-data-grid\.ui-plugins\.js$"      "cljsjs/react-data-grid/development/react-data-grid.ui-plugins.inc.js"
                         #"^react-data-grid.*/dist/react-data-grid\.css$"                 "cljsjs/react-data-grid/development/react-data-grid.inc.css"

                         #"^react-data-grid.*/dist/react-data-grid\.min\.js$"             "cljsjs/react-data-grid/production/react-data-grid.min.inc.js"
                         #"^react-data-grid.*/dist/react-data-grid\.ui-plugins\.min\.js$" "cljsjs/react-data-grid/production/react-data-grid.ui-plugins.min.inc.js"})
   (minify    :in       "cljsjs/react-data-grid/development/react-data-grid.inc.css"
              :out      "cljsjs/react-data-grid/production/react-data-grid.min.inc.css")
   (sift      :include  #{#"^cljsjs" #"^deps.cljs"})
   (pom)
   (jar)))
