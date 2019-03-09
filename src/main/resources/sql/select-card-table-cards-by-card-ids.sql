SELECT
  ctc.id,
  c.value,
  c.front_image,
  p.back_image,
  ctc.x_position,
  ctc.y_position,
  ctc.z_position,
  ctc.face_down,
  ctc.player_id
FROM card_table_card ctc
INNER JOIN card c 
ON ctc.card_id = c.id
INNER JOIN pack p 
ON ctc.pack_id = p.id
WHERE ctc.id = ANY(?)
ORDER BY ctc.z_position;