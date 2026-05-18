require 'dotenv/load'

db_name = ENV['DB_NAME']

command = <<~CMD
PGPASSWORD="#{ENV['DB_PASS']}" dropdb \
  --force \
  -h #{ENV['DB_HOST']} \
  -p #{ENV['DB_PORT']} \
  -U #{ENV['DB_USER']} \
  #{db_name}
CMD

puts "Ejecutando..."
success = system(command)

if success
  puts "Base '#{db_name}' eliminada"
else
  puts "No se pudo eliminar la base"
end


# ruby ruby/drop_database.rb