INSERT INTO card_table_card(
  card_table_id,
  card_id,
  pack_id,
  x_position,
  y_position,
  z_position)
SELECT
  ?, 
  card_id, 
  pack_id, 
  ?, 
  ?, 
  ROW_NUMBER() OVER (ORDER BY 1)
FROM pack_card
WHERE pack_id = ?;