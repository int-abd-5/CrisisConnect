-- ============================================
-- Make a User Admin
-- ============================================
-- Replace 'USER_EMAIL_HERE' with the email of the user you want to make admin

UPDATE public.profiles
SET is_admin = true
WHERE id = (
    SELECT id FROM auth.users WHERE email = 'USER_EMAIL_HERE'
);

-- Verify it worked:
SELECT id, full_name, email, is_admin 
FROM public.profiles 
WHERE id = (
    SELECT id FROM auth.users WHERE email = 'USER_EMAIL_HERE'
);

