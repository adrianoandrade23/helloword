-- Run in Supabase SQL editor
create table if not exists public.waitlist_signups (
  id uuid primary key default gen_random_uuid(),
  email text not null unique,
  source text not null default 'landing-page',
  created_at timestamptz not null default now()
);

alter table public.waitlist_signups enable row level security;

-- Allow anyone using the anon key to insert a waitlist signup.
do $$
begin
  if not exists (
    select 1
    from pg_policies
    where schemaname = 'public'
      and tablename = 'waitlist_signups'
      and policyname = 'allow_waitlist_insert_anon'
  ) then
    create policy allow_waitlist_insert_anon
      on public.waitlist_signups
      for insert
      to anon
      with check (true);
  end if;
end $$;

-- Optional: keep reads restricted to service role only (default with no select policy).
