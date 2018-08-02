Simple HSSFWorkbook wrapper class allowing you to parser from XLS to:
- Class with annotated fields and static bounds
- Class with annotated fields and dynamic bounds
- Generic list of values (String/Double)
- Generic matrix of values (String/Double)

Configuring the parser
- Static configuration of parsing area (cell start, cell end, row start, row end) on the class annotations
- Dynamic configuration of parsing area on the API