require 'roo'
require 'json'

archivo = 'scripts/ubicaciones.xlsx'
xlsx = Roo::Excelx.new(archivo)
sheet = xlsx.sheet(0)

estructura = {}

# Empezamos en la fila 15
# Columna B es la 2, C es la 3, D es la 4, etc.
(15..sheet.last_row).each do |i|

  # Leer columnas desde la B (índice 2)
  cod_dep  = sheet.cell(i, 'B').to_i
  nom_dep  = sheet.cell(i, 'C').to_s.strip

  cod_dist = sheet.cell(i, 'D').to_i
  nom_dist = sheet.cell(i, 'E').to_s.strip

  cod_loc  = sheet.cell(i, 'F').to_i
  nom_loc  = sheet.cell(i, 'G').to_s.strip

  # Seguridad: Si no hay código de departamento, saltar fila
  next if cod_dep == 0

  # Nivel 1: Departamento
  estructura[cod_dep] ||= {
    codigo: cod_dep,
    departamento: nom_dep,
    distritos: {}
  }

  # Nivel 2: Distrito
  estructura[cod_dep][:distritos][cod_dist] ||= {
    codigo: cod_dist,
    distrito: nom_dist,
    localidades: {}
  }

  # Nivel 3: Localidad (usamos nombre como llave por si los códigos de barrio se repiten)
  estructura[cod_dep][:distritos][cod_dist][:localidades][nom_loc] ||= {
    codigo: cod_loc,
    localidad: nom_loc
  }
end

# Transformación a Arrays
resultado = estructura.values.map do |dep|
  {
    codigo: dep[:codigo],
    departamento: dep[:departamento],
    distritos: dep[:distritos].values.map do |dist|
      {
        codigo: dist[:codigo],
        distrito: dist[:distrito],
        localidades: dist[:localidades].values
      }
    end
  }
end

# Guardar
File.open('scripts/ubicaciones.json', 'w:UTF-8') do |f|
  f.write(JSON.pretty_generate(resultado))
end

puts "Procesadas #{sheet.last_row - 14} filas. JSON generado en scripts/ubicaciones.json"